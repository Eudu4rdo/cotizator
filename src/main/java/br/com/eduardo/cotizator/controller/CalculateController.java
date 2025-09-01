package br.com.eduardo.cotizator.controller;

import br.com.eduardo.cotizator.dto.CalculateRequest;
import br.com.eduardo.cotizator.model.Stock;
import br.com.eduardo.cotizator.repository.StockRepository;
import br.com.eduardo.cotizator.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/calculate")
public class CalculateController {
    private final StockService stockService;
    private final StockRepository stockRepository;

    public CalculateController(StockService stockService, StockRepository stockRepository) {
        this.stockService = stockService;
        this.stockRepository = stockRepository;
    }

    @PostMapping()
    public ResponseEntity<?> calculate(@RequestBody @Validated CalculateRequest request) {
        double total_amount = request.getAmount();
        boolean strict_limit = request.isStrict_limit();
        List<Stock> stocks = stockRepository.findAll();
        if(stocks.isEmpty()){
            return ResponseEntity.status(404).body("É necessário cadastrar Ações para calcular carteira");
        }
        CalculateRequest response = new CalculateRequest();
        double total_expendend = 0;
        List<Stock> updatedStocks = new ArrayList<>();
        for (Stock stock : stocks) {
            int stock_qtd = stockService.calculateQtd(stock.getValue(), total_amount, stock.getPercentage(), strict_limit);
            stock.setQtd(stock_qtd);
            total_expendend += (stock_qtd * stock.getValue());
            updatedStocks.add(stockRepository.save(stock));
        }
        response.setAmount(total_amount);
        response.setStrict_limit(strict_limit);
        response.setTotal_expended(total_expendend);
        double variance = total_amount - total_expendend;
        BigDecimal bd = BigDecimal.valueOf(variance).setScale(2, RoundingMode.HALF_UP);
        response.setVariance(bd.doubleValue());
        response.setStocks(updatedStocks);
        return ResponseEntity.ok(response);
    }
}
