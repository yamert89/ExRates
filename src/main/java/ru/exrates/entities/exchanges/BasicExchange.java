package ru.exrates.entities.exchanges;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;

import java.util.*;

public abstract class BasicExchange implements Exchange {
    @Getter
    private TreeSet<CurrencyPair> pairs = new TreeSet<>((o1, o2) -> (int) (o1.getLastUse() - o2.getLastUse()));

    @Value("{pairs.size}")
    private int maxSize;



    public void insertPair(CurrencyPair pair){
        pairs.add(pair);
        if (pairs.size() > maxSize) pairs.pollLast();
    }



    @Override
    public abstract double currentPrice(Currency c1, Currency c2);

    @Override
    public abstract Queue<Double> priceHistory(Currency c1, Currency c2);

    @Override
    public abstract Map<String, Double> priceChange(Currency c1, Currency c2);
}
