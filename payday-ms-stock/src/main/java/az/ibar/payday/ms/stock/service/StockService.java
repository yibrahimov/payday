package az.ibar.payday.ms.stock.service;

import az.ibar.payday.ms.stock.model.StockTransactionRequest;
import az.ibar.payday.ms.stock.model.StockView;
import az.ibar.payday.ms.stock.model.UserStockView;

import java.util.List;

public interface StockService {
    List<StockView> findAll();

    List<StockView> findAllPreferential();

    void transaction(StockTransactionRequest transactionRequest);
}
