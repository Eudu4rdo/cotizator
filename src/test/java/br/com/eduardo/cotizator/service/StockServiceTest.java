package br.com.eduardo.cotizator.service;

import br.com.eduardo.cotizator.dto.StockApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private StockService stockService;

    @BeforeEach
    void setup() {
        stockService = new StockService("http://fake-api/", "fake-token", restTemplate);
    }

    @Test
    void shouldRoundDownWhenStrictLimitIsTrue() {
        int qtd = stockService.calculateQtd(51.0, 1000.0, 0.1, true);
        assertEquals(1, qtd);
    }

    @Test
    void shouldRoundUpWhenStrictLimitIsFalse() {
        int qtd = stockService.calculateQtd(51.0, 1000.0, 0.1, false);
        assertEquals(2, qtd);
    }

    @Test
    void shouldReturnZeroWhenAmountIsInsufficientAndStrictLimitIsTrue() {
        int qtd = stockService.calculateQtd(50, 49, 1, true);
        assertEquals(0, qtd);
    }

    @Test
    void shouldReturnOneWhenAmountIsInsufficientAndStrictLimitIsFalse() {
        int qtd = stockService.calculateQtd(50, 49, 1, false);
        assertEquals(1, qtd);
    }

    @Test
    void shouldReturnStockDataWhenValidCod() {
        StockApiResponse mockResponse = new StockApiResponse();
        StockApiResponse.Result stockData = new StockApiResponse.Result();
        stockData.setSymbol("PETR4");
        stockData.setRegularMarketPrice(27.45);
        mockResponse.setResults(List.of(stockData));

        ResponseEntity<StockApiResponse> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(StockApiResponse.class)
        )).thenReturn(responseEntity);

        StockApiResponse result = stockService.getStockData("PETR4");

        assertNotNull(result);
        assertEquals(1, result.getResults().size());
        assertEquals("PETR4", result.getResults().get(0).getSymbol());
        assertEquals(27.45, result.getResults().get(0).getRegularMarketPrice());
    }

    @Test
    void shouldReturnNullWhenApiThrowsException() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(StockApiResponse.class)
        )).thenThrow(new RuntimeException("API error"));

        StockApiResponse result = stockService.getStockData("PETR4");

        assertNull(result);
    }
}
