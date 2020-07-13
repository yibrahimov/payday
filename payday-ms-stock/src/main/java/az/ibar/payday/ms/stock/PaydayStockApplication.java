package az.ibar.payday.ms.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: Shahriyar Novruzov,
 * Date : 2020-07-12
 */
@EnableScheduling
@SpringBootApplication
public class PaydayStockApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaydayStockApplication.class, args);
    }
}

