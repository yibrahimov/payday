package az.ibar.payday.ms.notification.queue;

import az.ibar.payday.commons.model.RabbitMQueue;
import az.ibar.payday.commons.model.StockInfoDto;
import az.ibar.payday.ms.notification.logger.SafeLogger;
import az.ibar.payday.ms.notification.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitMQueue.STOCK_EMAIL_QUEUE)
public class MailQueueListener {
    private static final SafeLogger logger = SafeLogger.getLogger(MailQueueListener.class);

    private final EmailService emailService;

    public MailQueueListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitHandler
    public void receiveEmailMessage(StockInfoDto stockInfoDto) {

        logger.debug("Message received stock: {}", stockInfoDto);
        emailService.sendEmail(stockInfoDto);
    }
}
