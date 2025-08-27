package br.com.eduardo.cotizator.controller;

import br.com.eduardo.cotizator.dto.CalculateRequest;
import br.com.eduardo.cotizator.dto.StockApiResponse;
import br.com.eduardo.cotizator.model.Stock;
import br.com.eduardo.cotizator.repository.StockRepository;
import br.com.eduardo.cotizator.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        float total_amount = request.getAmount();
        boolean strict_limit = request.isStrict_limit();
        List<Stock> stocks = stockRepository.findAll();
        if(stocks.isEmpty()){
            return ResponseEntity.status(404).body("É necessário cadastrar Ações para calcular carteira");
        }

        CalculateRequest response = new CalculateRequest(strict_limit);
        double total_expendend = 0;
        for (Stock stock : stocks) {
            double stock_amount = total_amount * stock.getPercentage();
            double stock_value = stock.getValue();
            int stock_qtd;
            if (strict_limit) {
                stock_qtd = (int) Math.ceil(stock_amount / stock_value);
            } else {
                stock_qtd = (int) Math.floor(stock_amount / stock_value);
            }
            total_expendend += (stock_qtd * stock_value);
            stock.setQtd(stock_qtd);
            stock = stockRepository.save(stock);
        }
        response.setAmount(total_amount);
        response.setStrict_limit(strict_limit);
        response.setTotal_expended(total_expendend);
        response.setVariance(total_amount - total_expendend);
        response.setStocks(stocks);
        return ResponseEntity.ok(response);
    }
}