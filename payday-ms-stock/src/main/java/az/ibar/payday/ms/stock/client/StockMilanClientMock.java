package az.ibar.payday.ms.stock.client;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StockMilanClientMock implements StockClient{
    @Override
    public List<StockDto> getAvailableStocks() {
        List<StockDto> stocks = new ArrayList<>();
        stocks.add(StockClient.makeStock("3", "Pirelli", BigDecimal.valueOf(new Random().nextInt(8) + 1)));
        stocks.add(StockClient.makeStock("4", "Prada", BigDecimal.valueOf(new Random().nextInt(8) + 1)));
        return stocks;
    }
}
