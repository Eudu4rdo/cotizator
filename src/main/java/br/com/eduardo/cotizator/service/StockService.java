package br.com.eduardo.cotizator.service;

import br.com.eduardo.cotizator.dto.StockApiResponse;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockService {
    @Value("${b3.api.url}")
    private String api_url;
    @Value("${b3.api.token}")
    private String api_token;

    public StockApiResponse getStockData(String cod) {
        String url = this.api_url + "quote/" + cod;
        RestTemplate restTemplate = new RestTemplate();
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

}
