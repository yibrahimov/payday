package az.ibar.payday.ms.stock.client;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StockNYClientMock implements StockClient {
    @Override
    public List<StockDto> getAvailableStocks() {
        List<StockDto> stocks = new ArrayList<>();
        stocks.add(StockClient.makeStock("5", "Apple", BigDecimal.valueOf(new Random().nextInt(6) + 1)));
        stocks.add(StockClient.makeStock("6", "Ambak", BigDecimal.valueOf(new Random().nextInt(6) + 1)));
        return stocks;
    }
}
