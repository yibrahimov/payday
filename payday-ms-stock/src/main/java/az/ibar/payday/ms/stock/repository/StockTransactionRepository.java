package az.ibar.payday.ms.stock.repository;

import az.ibar.payday.ms.stock.entity.StockTransactionEntity;
import az.ibar.payday.ms.stock.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransactionEntity, Long> {
    StockTransactionEntity findByUserIdAndStockIdAndTransactionTypeAndIsSuccessful(Long userId, String stockId,
                                                                                   TransactionType transactionType,
                                                                                   boolean isSuccessful);

    List<StockTransactionEntity> findAllByUserId(Long userId);

    List<StockTransactionEntity> findAllByStockIdAndTransactionTypeAndIsSuccessful(String stockId,
                                                                                   TransactionType transactionType,
                                                                                   boolean isSuccessful);
}
