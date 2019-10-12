package ru.exrates.entities.exchanges;

import org.springframework.stereotype.Component;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;

import java.util.Map;
import java.util.Queue;

@Component
public interface Exchange {
    double currentPrice(Currency c1, Currency c2);
    Queue<Double> priceHistory(Currency c1, Currency c2);
    Map<String, Double> priceChange(Currency c1, Currency c2);
}
