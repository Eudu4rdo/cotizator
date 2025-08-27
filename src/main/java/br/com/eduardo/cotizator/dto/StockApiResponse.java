package br.com.eduardo.cotizator.dto;

import java.util.List;

public class StockApiResponse {
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public static class Result {
        private String symbol;
        private String shortName;
        private Double regularMarketPrice;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String name) {
            this.shortName = name;
        }

        public Double getRegularMarketPrice() {
            return regularMarketPrice;
        }

        public void setRegularMarketPrice(Double regularMarketPrice) {
            this.regularMarketPrice = regularMarketPrice;
        }
    }
}
