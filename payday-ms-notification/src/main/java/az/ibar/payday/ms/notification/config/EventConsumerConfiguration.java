package az.ibar.payday.ms.notification.config;

import az.ibar.payday.commons.model.RabbitMQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class EventConsumerConfiguration {

    @Bean
    public Queue queue() {
        return new Queue(RabbitMQueue.STOCK_EMAIL_QUEUE);
    }
}
