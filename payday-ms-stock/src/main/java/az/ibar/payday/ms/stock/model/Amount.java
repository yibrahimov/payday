package az.ibar.payday.ms.stock.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class Amount {
    private BigDecimal value;
    private String currency;
}
