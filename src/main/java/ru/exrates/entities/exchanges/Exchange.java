package ru.exrates.entities.exchanges;

import org.springframework.stereotype.Component;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;

import java.util.Map;
import java.util.Queue;

@Component
public interface Exchange {
    void insertPair(CurrencyPair pair);
    CurrencyPair getPair(Currency c1, Currency c2);

}
