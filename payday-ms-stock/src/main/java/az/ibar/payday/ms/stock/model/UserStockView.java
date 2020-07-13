package az.ibar.payday.ms.stock.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStockView {

    private String stockId;
    private String name;
    private Amount buyingPrice;
    private Amount currentPrice;
    private String percentage;
}
