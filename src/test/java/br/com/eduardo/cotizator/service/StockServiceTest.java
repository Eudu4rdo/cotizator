package br.com.eduardo.cotizator.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StockServiceTest {
    private final StockService stockService = new StockService();
    @Test
    void testCalculateQtdStrictLimitTrue() {
        int qtd = stockService.CalculateQtd(51.0, 1000.0, 0.1, true);
        assertEquals(2, qtd);
    }

    @Test
    void testCalculateQtdStrictLimitFalse() {
        int qtd = stockService.CalculateQtd(51.0, 1000.0, 0.1, false);
        assertEquals(1, qtd);
    }

    @Test
    void testCalculateQtdInsufficientAmountStrictLimitTrue() {
        int qtd = stockService.CalculateQtd(50, 49, 1, true);
        assertEquals(1, qtd);
    }

    @Test
    void testCalculateQtdInsufficientAmountStrictLimitFalse() {
        int qtd = stockService.CalculateQtd(50, 49, 1, false);
        assertEquals(0, qtd);
    }
}
