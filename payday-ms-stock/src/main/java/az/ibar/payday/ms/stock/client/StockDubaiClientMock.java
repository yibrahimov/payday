package az.ibar.payday.ms.stock.client;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StockDubaiClientMock implements StockClient {

    @Override
    public List<StockDto> getAvailableStocks() {
        List<StockDto> stocks = new ArrayList<>();
        stocks.add(StockClient.makeStock("1", "EIBANK", BigDecimal.valueOf(new Random().nextInt(5) + 1)));
        stocks.add(StockClient.makeStock("2", "AMLAK", BigDecimal.valueOf(new Random().nextInt(5) + 1)));
        return stocks;
    }
}
