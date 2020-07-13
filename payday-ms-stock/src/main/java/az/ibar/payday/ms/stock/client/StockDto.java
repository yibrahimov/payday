package az.ibar.payday.ms.stock.client;

import az.ibar.payday.ms.stock.model.Amount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDto {

    private String stockId;
    private String name;
    private BigDecimal price;
}
