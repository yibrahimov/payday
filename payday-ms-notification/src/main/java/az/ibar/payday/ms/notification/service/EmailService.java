package az.ibar.payday.ms.notification.service;

import az.ibar.payday.commons.model.StockInfoDto;

public interface EmailService {
    void sendEmail(StockInfoDto emailRequest);
}
