package ru.exrates.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import ru.exrates.entities.exchanges.secondary.collections.UpdateListenerMap;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

@Entity
@NoArgsConstructor
public class CurrencyPair implements Comparable<CurrencyPair>{
    @Id @GeneratedValue
    @JsonIgnore
    @Getter
    private int id;

    private Long version;

    @Column(unique = true, nullable = false)
    private String symbol;

    private double price;

    @ElementCollection
    @MapKeyColumn(name = "PERIOD")
    @Column(name = "VALUE")
    private Map<TimePeriod, Double> priceChange = new UpdateListenerMap<>(this);

    @ElementCollection
    private Collection<Double> priceHistory = new ArrayBlockingQueue<>(20, true);

    @Getter @Setter
    private Instant lastUse = Instant.now();

    /*
        indexes:
        0 - price
        1 - priceChange
        2 - priceHistory
     */
    @Getter
    private long[] updateTimes = new long[3];

    public CurrencyPair(Currency currency1, Currency currency2) {
        symbol = currency1.getSymbol() + currency2.getSymbol();
    }

    public CurrencyPair(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public int compareTo(CurrencyPair o) {
        return lastUse.isAfter(o.getLastUse()) ? 1 : -1;
    }

    @Override
    public String toString() {
        return symbol + " " + lastUse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyPair pair = (CurrencyPair) o;
        return id == pair.id &&
                symbol.equals(pair.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, symbol);
    }

    public double getPrice() {
        lastUse = Instant.now();
        return price;
    }

    public void setPrice(double price) {
        updateTimes[0] = Instant.now().toEpochMilli();
        this.price = price;
    }

    public Map<TimePeriod, Double> getPriceChange() {
        lastUse = Instant.now();
        return priceChange;
    }

    public Collection<Double> getPriceHistory() {
        lastUse = Instant.now();
        return priceHistory;
    }


}
