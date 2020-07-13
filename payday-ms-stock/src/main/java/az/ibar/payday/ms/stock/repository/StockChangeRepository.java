package az.ibar.payday.ms.stock.repository;

import az.ibar.payday.ms.stock.entity.StockChangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockChangeRepository extends JpaRepository<StockChangeEntity, Long> {
    List<StockChangeEntity> findAllByStockId(String stockId);
}
