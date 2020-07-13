package az.ibar.payday.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockInfoDto implements Serializable {

    private Long userId;
    private String stockId;
    private String stockName;
    private BigDecimal currentPrice;
    private BigDecimal buyingPrice;
    private BigDecimal sellingPrice;
    private BigDecimal percentage;
    private String operation;
    private String status;
}
