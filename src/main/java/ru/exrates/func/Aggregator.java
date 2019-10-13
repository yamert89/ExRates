package ru.exrates.func;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.BinanceExchange;
import ru.exrates.entities.exchanges.Exchange;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class Aggregator {
    private Map<String, Exchange> exchanges;

    private BinanceExchange exchange;

    @Autowired
    public void setExchange(BinanceExchange exchange) {
        this.exchange = exchange;
    }

    public Aggregator() {
        exchanges = new HashMap<>();
        exchanges.put("binance", exchange);
    }

    //private Set<Currency> currencies = new HashSet<>();

    public Exchange getExchange(String exName){
        return exchanges.get(exName);
    }

    public Set<CurrencyPair> getCurStat(String curName1, String curName2){
        var tempCur = new Currency(curName1);
        var tempCur2 = new Currency(curName2);
        var curs = new HashSet<CurrencyPair>();
        exchanges.forEach((key, val) -> {
            curs.add(val.getPair(tempCur, tempCur2));
        });
        return curs;
    }

}
