package br.com.eduardo.cotizator.dto;

import br.com.eduardo.cotizator.model.Stock;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

public class CalculateRequest {
    @NotNull
    private double amount;
    private Double total_expended;
    private Double variance;
    @NotNull
    private boolean strict_limit;
    private List<Stock> stocks;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotal_expended() {
        return total_expended;
    }

    public void setTotal_expended(double total_expended) {
        this.total_expended = total_expended;
    }

    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {
        this.variance = variance;
    }

    public boolean isStrict_limit() {
        return strict_limit;
    }

    public void setStrict_limit(boolean strict_limit) {
        this.strict_limit = strict_limit;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
