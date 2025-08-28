package br.com.eduardo.cotizator.controller;

import br.com.eduardo.cotizator.dto.CalculateRequest;
import br.com.eduardo.cotizator.dto.StockApiResponse;
import br.com.eduardo.cotizator.model.Stock;
import br.com.eduardo.cotizator.repository.StockRepository;
import br.com.eduardo.cotizator.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api") // rota base
public class StocksController {
    private final StockService stockService;
    private final StockRepository stockRepository;

    public StocksController(StockService stockService, StockRepository stockRepository) {
        this.stockService = stockService;
        this.stockRepository = stockRepository;
    }

    @GetMapping
    public ResponseEntity<List<Stock>> list() {
        List<Stock> stocks = stockRepository.findAll();
        if (stocks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(stocks);
    }

    @PostMapping
    public ResponseEntity<?> upset(@RequestBody @Validated Stock request) {
        StockApiResponse stockData = stockService.getStockData(request.getCod());
        if (stockData == null) {
            return ResponseEntity.status(404).body("Ação não encontrada");
        }
        Optional<Stock> optionalStock = stockRepository.findByCod(request.getCod());
        Stock stock;
        if (optionalStock.isPresent()) {
            stock = optionalStock.get();
        } else {
            stock = new Stock();
            stock.setCod(request.getCod());
        }
        stock.setName(stockData.getResults().getFirst().getShortName());
        stock.setPercentage(request.getPercentage()/100);
        stock.setValue(stockData.getResults().getFirst().getRegularMarketPrice());
        stock = stockRepository.save(stock);
        return ResponseEntity.ok(stock);
    }

    @PostMapping("calculate")
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