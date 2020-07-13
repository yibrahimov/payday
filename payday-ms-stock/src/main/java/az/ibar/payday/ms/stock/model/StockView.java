package az.ibar.payday.ms.stock.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockView {

    private String stockId;
    private String name;
    private Amount price;
}
