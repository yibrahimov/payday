package az.ibar.payday.ms.stock.helper;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class StockPercentageHelper {

    public BigDecimal calculatePercentage(BigDecimal buying, BigDecimal current) {
        BigDecimal diff;
        if (buying.compareTo(current) < 0) {
            diff = current.subtract(buying);
            return diff.multiply(BigDecimal.valueOf(100)).divide(buying, 2, RoundingMode.HALF_UP);
        } else if (buying.compareTo(current) > 0) {
            diff = buying.subtract(current);
            return diff.multiply(BigDecimal.valueOf(100)).divide(buying, 2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
