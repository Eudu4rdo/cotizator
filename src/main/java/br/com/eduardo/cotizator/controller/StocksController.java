package br.com.eduardo.cotizator.controller;

import br.com.eduardo.cotizator.dto.StockApiResponse;
import br.com.eduardo.cotizator.model.Stock;
import br.com.eduardo.cotizator.repository.StockRepository;
import br.com.eduardo.cotizator.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock") // rota base
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

    @DeleteMapping("/{cod}")
    public ResponseEntity<?> delete(@PathVariable String cod) {
        Optional<Stock> stockData = stockRepository.findByCod(cod);
        if (stockData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ação não encontrada");
        }
        stockRepository.delete(stockData.get());
        return ResponseEntity.ok("Ação deletada com sucesso");
    }


}