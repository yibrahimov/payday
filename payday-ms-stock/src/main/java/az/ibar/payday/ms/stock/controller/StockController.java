package az.ibar.payday.ms.stock.controller;

import az.ibar.payday.ms.stock.model.StockTransactionRequest;
import az.ibar.payday.ms.stock.model.StockView;
import az.ibar.payday.ms.stock.model.UserStockView;
import az.ibar.payday.ms.stock.service.StockListService;
import az.ibar.payday.ms.stock.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/stock")
@Api(value = "Stock Controller: stock operations")
public class StockController {

    private final StockService stockService;
    private final StockListService stockListService;

    public StockController(StockService stockService,
                           StockListService stockListService) {
        this.stockService = stockService;
        this.stockListService = stockListService;
    }

    @ApiOperation(value = "Stock execute")
    @PostMapping("/transaction")
    public void transaction(@RequestBody StockTransactionRequest transactionRequest) {
        stockService.transaction(transactionRequest);
    }

    @ApiOperation(value = "Get all stocks")
    @GetMapping("/stocks")
    public List<StockView> getStocks() {
        return stockService.findAll();
    }

    @ApiOperation(value = "Get all preferential payments")
    @GetMapping("/stocks/preferential")
    public List<StockView> getPreferentialStocks() {
        return stockService.findAllPreferential();
    }

    @ApiOperation(value = "Get user's stocks")
    @GetMapping("users/{userId}/stocks")
    public List<UserStockView> getUserStocks(@PathVariable("userId") Long userId) {
        return stockListService.findUserStocks(userId);
    }
}
