package az.ibar.payday.ms.stock.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public interface StockClient {

    List<StockDto> getAvailableStocks();

    public static StockDto makeStock(String stockId, String name, BigDecimal price) {
        return StockDto
                .builder()
                .name(name)
                .stockId(stockId)
                .price(price)
                .build();
    }
}
