package az.ibar.payday.ms.stock.queue;

import az.ibar.payday.commons.model.StockInfoDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StockEmailSender {

    private RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.stock.mail}")
    String queueName;

    public StockEmailSender(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    public void produceMessage(StockInfoDto stockInfo) {

        rabbitTemplate.convertAndSend(queueName, stockInfo);

        System.out.println("SENTTTTTTTTTTTTTTT " + stockInfo.getStockId());
    }
}
