package br.com.eduardo.cotizator.service;

import br.com.eduardo.cotizator.dto.StockApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockService {
    private final String api_url;
    private final String api_token;
    private final RestTemplate restTemplate;

    public StockService(@Value("${b3.api.url}") String api_url, @Value("${b3.api.token}") String api_token, RestTemplate restTemplate) {
        this.api_url = api_url;
        this.api_token = api_token;
        this.restTemplate = restTemplate;
    }

    public StockApiResponse getStockData(String cod) {
        String url = this.api_url + "quote/" + cod;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + this.api_token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<StockApiResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    StockApiResponse.class
            );
            StockApiResponse body = response.getBody();
            if (body != null && body.getResults() != null && !body.getResults().isEmpty()) {
                return body;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer calculateQtd(double stock_value, double total_amount, double stock_percentage, boolean strict_limit) {
        double stock_amount = total_amount * stock_percentage;
        if (strict_limit) {
            return (int) Math.floor(stock_amount / stock_value);
        } else {
            return (int) Math.ceil(stock_amount / stock_value);
        }
    }
}