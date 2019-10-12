package ru.exrates.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Queue;

public class CurrencyPair {

    private String symbol;
    @Getter @Setter
    private double price;
    @Getter
    private Map<String, Double> priceChange;
    @Getter
    private Queue<Double> priceHistory;
    @Getter @Setter
    private long lastUse;

    public CurrencyPair(Currency currency1, Currency currency2) {
        symbol = currency1.getSymbol() + currency2.getSymbol();
    }

    public String getSymbol() {
        lastUse = System.currentTimeMillis();
        return symbol;
    }
}
