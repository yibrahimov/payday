package az.ibar.payday.ms.stock.scheduler;

import az.ibar.payday.ms.stock.logger.SafeLogger;
import az.ibar.payday.ms.stock.service.StockListService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StockListScheduler {

    private static final SafeLogger logger = SafeLogger.getLogger(StockListScheduler.class);

    private final StockListService stockListService;

    public StockListScheduler(StockListService stockListService) {
        this.stockListService = stockListService;
    }

    @Scheduled(cron = "${scheduler.stock.cron}")
    public void checkClientDuplicates() {
        logger.info("Retrieving stocks scheduled job start");

        stockListService.retrieveAvailableStocks();

        logger.info("Retrieving stocks scheduled job end");
    }
}
