package ru.exrates.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

@Entity
public class CurrencyPair implements Comparable<CurrencyPair>{
    @Id @GeneratedValue
    @JsonIgnore
    @Getter
    private int id;

    private Long version;

    @Column(unique = true, nullable = false)
    private String symbol;

    @Getter @Setter
    private double price;

    @Getter
    @ElementCollection
    @MapKeyColumn(name = "PERIOD")
    @Column(name = "VALUE")
    private Map<TimePeriod, Double> priceChange = new HashMap<>();

    @Getter
    @ElementCollection
    private Collection<Double> priceHistory = new ArrayBlockingQueue<>(20, true);

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
