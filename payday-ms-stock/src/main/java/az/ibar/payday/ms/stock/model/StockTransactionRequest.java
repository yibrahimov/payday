package az.ibar.payday.ms.stock.model;

import az.ibar.payday.ms.stock.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockTransactionRequest {

    private Long userId;
    private String stockId;
    private TransactionType transactionType;
    private Amount maximumPrice;
    private String urlHook;
}
