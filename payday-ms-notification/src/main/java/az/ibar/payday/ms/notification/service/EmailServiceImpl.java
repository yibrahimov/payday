package az.ibar.payday.ms.notification.service;

import az.ibar.payday.commons.model.StockInfoDto;
import az.ibar.payday.ms.notification.logger.SafeLogger;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final SafeLogger logger = SafeLogger.getLogger(EmailServiceImpl.class);

    private final String sendGridApiKey;

    public EmailServiceImpl(@Value("${sendgrid.apiKey}") String sendGridApiKey) {
        this.sendGridApiKey = sendGridApiKey;
    }

    @Override
    public void sendEmail(StockInfoDto emailRequest) {
        logger.debug("Sending email starting to userId: {}" + emailRequest.getUserId());

        Email from = new Email("sehriyarnovruzov@gmail.com");
        String subject = "PayDay - Stock info";
        Email to = new Email("sehriyarnovruzov@gmail.com");

        String body = "stockName: " + emailRequest.getStockName() + "\n" +
                "stockId: " + emailRequest.getStockId() + "\n" +
                "currentPrice: " + emailRequest.getCurrentPrice() + "\n" +
                "buyingPrice: " + emailRequest.getBuyingPrice() + "\n" +
                "sellingPrice: " + emailRequest.getSellingPrice() + "\n" +
                "percentage: " + emailRequest.getPercentage() + "\n" +
                "status: " + emailRequest.getStatus() + "\n";

        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            logger.debug("Sending email finished to userId: {}, responseCode: {}" + emailRequest.getUserId(),
                    response.getStatusCode());
        } catch (Exception ex) {
            logger.error("error while sending email: {}", ex);
        }
    }
}
