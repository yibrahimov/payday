package az.ibar.payday.ms.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author: Shahriyar Novruzov,
 * Date : 2020-07-12
 */
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class PaydayApiGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaydayApiGatewayServiceApplication.class, args);
    }
}
