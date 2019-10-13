package ru.exrates.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.Exchange;
import ru.exrates.func.Aggregator;

import java.util.Set;

@RestController
public class RestInfo {

    private Aggregator aggregator;

    @Autowired
    public void setAggregator(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    @GetMapping("/rest/exchange")
    public Exchange getExchange(@RequestParam String exchange){
        return aggregator.getExchange(exchange);
    }

    @GetMapping("/rest/pair")
    public Set<CurrencyPair> pair(@RequestParam String c1, @RequestParam String c2){
        return aggregator.getCurStat(c1, c2);
    }

}
