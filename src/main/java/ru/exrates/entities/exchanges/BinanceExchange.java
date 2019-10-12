package ru.exrates.entities.exchanges;

import lombok.Getter;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;

import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BinanceExchange extends BasicExchange {

    @Override
    public double currentPrice(Currency c1, Currency c2) {
        return 0;
    }

    @Override
    public Queue<Double> priceHistory(Currency c1, Currency c2) {
        return null;
    }

    @Override
    public Map<String, Double> priceChange(Currency c1, Currency c2) {
        return null;
    }
}
