package az.ibar.payday.ms.stock.service.impl;

import az.ibar.payday.commons.model.StockInfoDto;
import az.ibar.payday.ms.stock.entity.StockChangeEntity;
import az.ibar.payday.ms.stock.entity.StockEntity;
import az.ibar.payday.ms.stock.entity.StockTransactionEntity;
import az.ibar.payday.ms.stock.exception.NotFoundException;
import az.ibar.payday.ms.stock.helper.StockPercentageHelper;
import az.ibar.payday.ms.stock.logger.SafeLogger;
import az.ibar.payday.ms.stock.mapper.StockMapper;
import az.ibar.payday.ms.stock.model.StockTransactionRequest;
import az.ibar.payday.ms.stock.model.StockView;
import az.ibar.payday.ms.stock.model.enums.OperationStatus;
import az.ibar.payday.ms.stock.model.enums.StockOperation;
import az.ibar.payday.ms.stock.model.enums.TransactionType;
import az.ibar.payday.ms.stock.queue.StockEmailSender;
import az.ibar.payday.ms.stock.repository.StockChangeRepository;
import az.ibar.payday.ms.stock.repository.StockRepository;
import az.ibar.payday.ms.stock.repository.StockTransactionRepository;
import az.ibar.payday.ms.stock.service.StockService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {

    private static final SafeLogger logger = SafeLogger.getLogger(StockServiceImpl.class);

    private final StockRepository stockRepository;
    private final StockTransactionRepository transactionRepository;
    private final StockEmailSender messageProducer;
    private final StockChangeRepository stockChangeRepository;
    private final StockPercentageHelper percentageHelper;

    public StockServiceImpl(StockRepository stockRepository,
                            StockTransactionRepository transactionRepository,
                            StockEmailSender messageProducer,
                            StockChangeRepository stockChangeRepository,
                            StockPercentageHelper percentageHelper) {
        this.stockRepository = stockRepository;
        this.transactionRepository = transactionRepository;
        this.messageProducer = messageProducer;
        this.stockChangeRepository = stockChangeRepository;
        this.percentageHelper = percentageHelper;
    }

    @Override
    public List<StockView> findAll() {

        var stockEntities = stockRepository.findAll();
        return stockEntities
                .stream()
                .map(StockMapper.INSTANCE::stockViewFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockView> findAllPreferential() {
        List<StockEntity> stocks = stockRepository.findAll();

        return stocks
                .stream()
                .filter(s -> s.getPrice().compareTo(BigDecimal.valueOf(3)) >= 0 && checkStockChange(s))
                .map(StockMapper.INSTANCE::stockViewFromEntity)
                .collect(Collectors.toList());
    }

    public boolean checkStockChange(StockEntity stock) {
        List<StockChangeEntity> stockChanges = stockChangeRepository.findAllByStockId(stock.getStockId());
        stockChanges = stockChanges
                .stream()
                .filter(s -> s.getChangeDate().isAfter(LocalDateTime.now().minusDays(5)))
                .collect(Collectors.toList());

        if (stockChanges.isEmpty()) return false;

        BigDecimal minPrice = stockChanges
                .stream()
                .map(StockChangeEntity::getPrice)
                .min(Comparator.naturalOrder()).get();

        BigDecimal maxPrice = stockChanges
                .stream()
                .map(StockChangeEntity::getPrice)
                .max(Comparator.naturalOrder()).get();

        return percentageHelper.calculatePercentage(minPrice, maxPrice).compareTo(BigDecimal.valueOf(5)) <= 0;
    }

    @Override
    public void transaction(StockTransactionRequest transactionRequest) {

        switch (transactionRequest.getTransactionType()) {
            case BUY:
                buyStock(transactionRequest);
                break;
            case SELL:
                sellStock(transactionRequest);
                break;
            default:
                throw new IllegalArgumentException("no such transaction type");
        }
    }

    private void buyStock(StockTransactionRequest transactionRequest) {

        StockEntity stockEntity = stockRepository.findByStockId(transactionRequest.getStockId());

        if (stockEntity == null)
            throw new NotFoundException("stock with id: " + transactionRequest.getStockId() + " not found");

        StockTransactionEntity stockTransaction = transactionRepository
                .findByUserIdAndStockIdAndTransactionTypeAndIsSuccessful(transactionRequest.getUserId(),
                        transactionRequest.getStockId(), transactionRequest.getTransactionType(), true);

        if (stockTransaction != null)
            throw new IllegalArgumentException("already bought the stock id with: " + stockEntity.getStockId());

        // bid price should be more than current stock price
        boolean isSuccessful = transactionRequest.getMaximumPrice().getValue()
                .compareTo(stockEntity.getPrice()) >= 0;

        makeTransaction(transactionRequest, stockEntity, isSuccessful);
        messageProducer.produceMessage(StockInfoDto
                .builder()
                .stockId(stockEntity.getStockId())
                .buyingPrice(transactionRequest.getMaximumPrice().getValue())
                .currentPrice(stockEntity.getPrice())
                .stockName(stockEntity.getName())
                .status(isSuccessful ? OperationStatus.SUCCESS.name() : OperationStatus.FAIL.name())
                .operation(StockOperation.BUYING.name())
                .userId(transactionRequest.getUserId())
                .build()
        );
    }

    private void sellStock(StockTransactionRequest transactionRequest) {

        StockTransactionEntity stockTransaction = transactionRepository
                .findByUserIdAndStockIdAndTransactionTypeAndIsSuccessful(transactionRequest.getUserId(),
                        transactionRequest.getStockId(), TransactionType.BUY, true);
        if (stockTransaction == null)
            throw new NotFoundException("stock transaction not found");

        StockEntity stockEntity = stockRepository.findByStockId(transactionRequest.getStockId());
        if (stockEntity == null)
            throw new NotFoundException("stock with id: " + transactionRequest.getStockId() + " not found");

        stockTransaction.setTransactionType(TransactionType.SELL);
        transactionRepository.save(stockTransaction);

        messageProducer.produceMessage(StockInfoDto
                .builder()
                .stockId(stockEntity.getStockId())
                .buyingPrice(stockTransaction.getPrice())
                .sellingPrice(transactionRequest.getMaximumPrice().getValue())
                .currentPrice(stockEntity.getPrice())
                .stockName(stockEntity.getName())
                .status(OperationStatus.SUCCESS.name())
                .operation(StockOperation.SELLING.name())
                .userId(transactionRequest.getUserId())
                .build()
        );
    }

    private void makeTransaction(StockTransactionRequest transactionRequest, StockEntity stock, boolean isSuccessful) {
        StockTransactionEntity transactionEntity = StockTransactionEntity
                .builder()
                .stockId(stock.getStockId())
                .currency(stock.getCurrency())
                .price(transactionRequest.getMaximumPrice().getValue())
                .userId(transactionRequest.getUserId())
                .transactionType(transactionRequest.getTransactionType())
                .isSuccessful(isSuccessful).build();

        transactionRepository.save(transactionEntity);
    }
}
