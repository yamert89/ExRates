package ru.exrates.func;

import ru.exrates.entities.exchanges.Exchange;

import java.util.Map;
import java.util.Set;

public class Aggregator {
    private Map<String, Exchange> exchanges;

    public Exchange getExchange(String exName){
        return exchanges.get(exName);
    }

}
