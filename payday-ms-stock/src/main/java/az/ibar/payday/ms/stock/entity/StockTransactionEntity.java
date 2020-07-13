package az.ibar.payday.ms.stock.entity;

import az.ibar.payday.ms.stock.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "stock_transaction")
public class StockTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "stock_id")
    private String stockId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "currency")
    private String currency;
    @Column(name = "is_successful")
    private Boolean isSuccessful;
    @Column(name = "transaction_date")
    @CreationTimestamp
    private LocalDateTime transactionDate;
}
