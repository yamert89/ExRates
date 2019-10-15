package ru.exrates.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class CurrencyPair implements Comparable<CurrencyPair>{

    private String symbol;
    @Getter @Setter
    private double price;
    @Getter
    private Map<String, Double> priceChange = new HashMap<>();
    @Getter
    private Queue<Double> priceHistory = new ArrayBlockingQueue<>(20, true);
    @Getter @Setter
    private long lastUse = System.currentTimeMillis();

    public CurrencyPair(Currency currency1, Currency currency2) {
        symbol = currency1.getSymbol() + currency2.getSymbol();
    }

    public CurrencyPair(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        lastUse = System.currentTimeMillis();
        return symbol;
    }

    @Override
    public int compareTo(CurrencyPair o) {
        return (int) (lastUse - o.getLastUse());
    }

    @Override
    public String toString() {
        return symbol + " " + lastUse;
    }
}
