package az.ibar.payday.ms.stock.service.impl;

import az.ibar.payday.commons.model.StockInfoDto;
import az.ibar.payday.ms.stock.client.StockClient;
import az.ibar.payday.ms.stock.client.StockDto;
import az.ibar.payday.ms.stock.entity.StockChangeEntity;
import az.ibar.payday.ms.stock.entity.StockEntity;
import az.ibar.payday.ms.stock.entity.StockTransactionEntity;
import az.ibar.payday.ms.stock.exception.NotFoundException;
import az.ibar.payday.ms.stock.helper.StockPercentageHelper;
import az.ibar.payday.ms.stock.logger.SafeLogger;
import az.ibar.payday.ms.stock.model.Amount;
import az.ibar.payday.ms.stock.model.UserStockView;
import az.ibar.payday.ms.stock.model.enums.Currency;
import az.ibar.payday.ms.stock.model.enums.StockOperation;
import az.ibar.payday.ms.stock.model.enums.TransactionType;
import az.ibar.payday.ms.stock.queue.StockEmailSender;
import az.ibar.payday.ms.stock.repository.StockChangeRepository;
import az.ibar.payday.ms.stock.repository.StockRepository;
import az.ibar.payday.ms.stock.repository.StockTransactionRepository;
import az.ibar.payday.ms.stock.service.StockListService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockListServiceImpl implements StockListService {

    private static final SafeLogger logger = SafeLogger.getLogger(StockListServiceImpl.class);

    private final StockRepository stockRepository;
    private final StockTransactionRepository transactionRepository;
    private final StockEmailSender messageProducer;
    private final StockClient stockClientNY;
    private final StockClient stockClientDubai;
    private final StockClient stockClientMilan;
    private final StockPercentageHelper percentageHelper;
    private final StockChangeRepository stockChangeRepository;

    public StockListServiceImpl(StockRepository stockRepository,
                                StockTransactionRepository transactionRepository,
                                StockEmailSender messageProducer,
                                @Qualifier("stockNYClientMock") StockClient stockClientNY,
                                @Qualifier("stockDubaiClientMock") StockClient stockClientDubai,
                                @Qualifier("stockMilanClientMock") StockClient stockClientMilan,
                                StockPercentageHelper percentageHelper,
                                StockChangeRepository stockChangeRepository) {
        this.stockRepository = stockRepository;
        this.transactionRepository = transactionRepository;
        this.messageProducer = messageProducer;
        this.stockClientNY = stockClientNY;
        this.stockClientDubai = stockClientDubai;
        this.stockClientMilan = stockClientMilan;
        this.percentageHelper = percentageHelper;
        this.stockChangeRepository = stockChangeRepository;
    }

    @PostConstruct
    public void initStocks() {
        retrieveAvailableStocks();
    }

    @Override
    public List<UserStockView> findUserStocks(Long userId) {

        List<StockTransactionEntity> stockTransactions = transactionRepository.findAllByUserId(userId);
        stockTransactions = stockTransactions
                .stream()
                .filter(t -> t.getIsSuccessful() && t.getTransactionType().equals(TransactionType.BUY))
                .collect(Collectors.toList());

        List<StockEntity> stocks = stockRepository.findAll();

        return getUserStocks(stockTransactions, stocks);
    }

    @Override
    public void retrieveAvailableStocks() {
        retrieveNYStocks();
        retrieveDubaiStocks();
        retrieveMilanStocks();
    }

    private void retrieveNYStocks() {
        try {
            List<StockDto> availableStocks = stockClientNY.getAvailableStocks();
            availableStocks
                    .forEach(stock -> {
                        saveStock(stock, Currency.USD.name());
                        saveStockChange(stock);
                    });
        } catch (Exception e) {
            logger.error("retrieve NY stocks error: {}", e);
        }
    }

    private void retrieveDubaiStocks() {
        try {
            List<StockDto> availableStocks = stockClientDubai.getAvailableStocks();
            availableStocks
                    .forEach(stock -> {
                        saveStock(stock, Currency.AED.name());
                        saveStockChange(stock);
                    });
        } catch (Exception e) {
            logger.error("retrieve Dubai stocks error: {}", e);
        }
    }

    private void retrieveMilanStocks() {
        try {
            List<StockDto> availableStocks = stockClientMilan.getAvailableStocks();
            availableStocks
                    .forEach(stock -> {
                        saveStock(stock, Currency.EUR.name());
                        saveStockChange(stock);
                    });
        } catch (Exception e) {
            logger.error("retrieve Milan stocks error: {}", e);
        }
    }

    private void saveStock(StockDto stock, String currency) {
        StockEntity stockEntity = stockRepository.findByStockId(stock.getStockId());
        if (stockEntity == null)
            stockEntity = StockEntity
                    .builder()
                    .stockId(stock.getStockId())
                    .currency(currency)
                    .name(stock.getName())
                    .build();
        stockEntity.setPrice(stock.getPrice());
        stockRepository.save(stockEntity);

        checkGainOrLoss(stockEntity);
    }

    private void saveStockChange(StockDto stock) {

        StockChangeEntity stockChangeEntity = StockChangeEntity
                .builder()
                .stockId(stock.getStockId())
                .price(stock.getPrice())
                .build();
        stockChangeRepository.save(stockChangeEntity);
    }

    private void checkGainOrLoss(StockEntity stockEntity) {
        List<StockTransactionEntity> transactions = transactionRepository
                .findAllByStockIdAndTransactionTypeAndIsSuccessful(stockEntity.getStockId(), TransactionType.BUY, true);

        transactions
                .forEach(t -> {

                    BigDecimal currentPrice = stockEntity.getPrice();
                    BigDecimal buyingPrice = t.getPrice();

                    StockInfoDto stockInfo = StockInfoDto
                            .builder()
                            .buyingPrice(buyingPrice)
                            .currentPrice(currentPrice)
                            .stockId(stockEntity.getStockId())
                            .stockName(stockEntity.getName())
                            .build();

                    if (currentPrice.divide(buyingPrice, 2, RoundingMode.HALF_UP)
                            .compareTo(BigDecimal.valueOf(1.5)) >= 0) {
                        stockInfo.setOperation(StockOperation.GAIN.name());
                        stockInfo.setPercentage(percentageHelper.calculatePercentage(buyingPrice, currentPrice));
                        stockInfo.setUserId(t.getUserId());
                        messageProducer.produceMessage(stockInfo);
                    } else if (buyingPrice.divide(currentPrice, 2, RoundingMode.HALF_UP)
                            .compareTo(BigDecimal.valueOf(2)) >= 0) {
                        stockInfo.setOperation(StockOperation.LOSS.name());
                        stockInfo.setPercentage(percentageHelper.calculatePercentage(buyingPrice, currentPrice));
                        stockInfo.setUserId(t.getUserId());
                        messageProducer.produceMessage(stockInfo);
                    }
                });
    }

    private List<UserStockView> getUserStocks(List<StockTransactionEntity> stockTransactions,
                                              List<StockEntity> stocks) {

        return
                stockTransactions
                        .stream()
                        .map(t -> {
                            StockEntity stock = stocks
                                    .stream()
                                    .filter(s -> s.getStockId().equals(t.getStockId()))
                                    .findAny()
                                    .orElseThrow(
                                            () -> new NotFoundException("stock not found with id: " + t.getStockId()));
                            Amount currentPrice = Amount
                                    .builder()
                                    .currency(stock.getCurrency())
                                    .value(stock.getPrice())
                                    .build();

                            Amount buyingPrice = Amount
                                    .builder()
                                    .currency(t.getCurrency())
                                    .value(t.getPrice())
                                    .build();

                            String percentage = currentPrice.getValue()
                                    .compareTo(buyingPrice.getValue()) < 0 ? "-" : "+";
                            percentage = percentage.concat(percentageHelper
                                    .calculatePercentage(buyingPrice.getValue(), currentPrice.getValue()).toString());

                            return UserStockView
                                    .builder()
                                    .stockId(stock.getStockId())
                                    .currentPrice(currentPrice)
                                    .buyingPrice(buyingPrice)
                                    .percentage(percentage)
                                    .name(stock.getName())
                                    .build();
                        }).collect(Collectors.toList());
    }
}
