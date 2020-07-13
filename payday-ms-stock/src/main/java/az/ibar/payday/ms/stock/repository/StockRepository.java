package az.ibar.payday.ms.stock.repository;

import az.ibar.payday.ms.stock.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    StockEntity findByStockId(String stockId);
}
