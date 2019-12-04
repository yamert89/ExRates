package ru.exrates.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.*;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.Exchange;
import ru.exrates.func.Aggregator;
import ru.exrates.utils.JsonTemplates;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RestController
public class RestInfo {

    private final static Logger logger = LogManager.getLogger(RestInfo.class);

    private Aggregator aggregator;
    private ObjectMapper objectMapper;

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setAggregator(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    /*
        {"exchange" : "binanceExchange", "timeout": "3m", "pairs" : ["btcusd", "etcbtc"]}
        //todo pairs - list of favorite pairs in bd for each user
     */

    @PostMapping("/rest/exchange")
    public Exchange getExchange(@RequestBody JsonTemplates.ExchangePayload exchangePayload){
        //JsonTemplates.ExchangePayload exchangePayload = null;
        logger.debug("payload = " + exchangePayload);
        try {
            //exchangePayload = objectMapper.readValue(payload, JsonTemplates.ExchangePayload.class);
            if (exchangePayload == null) throw new IOException();
        } catch (IOException e) {
            logger.error("error read Json" , e); //todo exc to client
            return null;
        }

        var ex = exchangePayload.getPairs().length > 0 ?
                aggregator.getExchange(exchangePayload.getExchange(), exchangePayload.getPairs(), exchangePayload.getTimeout()) :
                aggregator.getExchange(exchangePayload.getExchange());
        try {
            logger.debug("Exchange response: " + new ObjectMapper().writeValueAsString(ex));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ex;


    }

    @GetMapping("/rest/pair")
    public Map<String, CurrencyPair> pair(@RequestParam(required = false) String c1, @RequestParam(required = false) String c2,
                                          @RequestParam(required = false) String pname){
        logger.debug("c1 = [" + c1 + "], c2 = [" + c2 + "], pname = [" + pname + "]");
        return pname == null ? aggregator.getCurStat(c1, c2) : aggregator.getCurStat(pname);
    }

    @GetMapping("/service/save")
    public void save(){
        aggregator.save();
    }

    @GetMapping("/close")
    public void close(){
        context.close();
    }

    /*
    Exchange response: {"id":1,"temporary":false,"name":"binanceExchange",
    "pairs":[
        {
            "symbol":"YOYOBNB",
            "price":8.37E-4,
            "priceChange":{
                "TimePeriod{id=3, period=PT5M, name='5m'}":8.37E-4,
                "TimePeriod{id=11, period=PT24H, name='1d'}":8.29E-4,
                "TimePeriod{id=12, period=PT72H, name='3d'}":8.355E-4,
                "TimePeriod{id=9, period=PT8H, name='8h'}":8.37E-4,
                "TimePeriod{id=7, period=PT4H, name='4h'}":8.37E-4,
                "TimePeriod{id=2, period=PT3M, name='3m'}":8.37E-4,
                "TimePeriod{id=10, period=PT12H, name='12h'}":8.455E-4,
                "TimePeriod{id=12, period=PT72H, name='3d'}":8.355E-4,
                "TimePeriod{id=14, period=PT720H, name='1M'}":8.73E-4,
                "TimePeriod{id=8, period=PT6H, name='6h'}":8.455E-4,
                "TimePeriod{id=2, period=PT3M, name='3m'}":8.37E-4,
                "TimePeriod{id=13, period=PT168H, name='1w'}":8.355E-4,
                "TimePeriod{id=4, period=PT15M, name='15m'}":8.37E-4,
                "TimePeriod{id=9, period=PT8H, name='8h'}":8.37E-4,
                "TimePeriod{id=14, period=PT720H, name='1M'}":8.73E-4,
                "TimePeriod{id=7, period=PT4H, name='4h'}":8.37E-4,
                "TimePeriod{id=4, period=PT15M, name='15m'}":8.37E-4,
                "TimePeriod{id=13, period=PT168H, name='1w'}":8.355E-4,
                "TimePeriod{id=6, period=PT1H, name='1h'}":8.37E-4,
                "TimePeriod{id=11, period=PT24H, name='1d'}":8.29E-4,
                "TimePeriod{id=3, period=PT5M, name='5m'}":8.37E-4,
                "TimePeriod{id=8, period=PT6H, name='6h'}":8.37E-4,
                "TimePeriod{id=5, period=PT30M, name='30m'}":8.37E-4,
                "TimePeriod{id=5, period=PT30M, name='30m'}":8.37E-4,
                "TimePeriod{id=10, period=PT12H, name='12h'}":8.455E-4,
                "TimePeriod{id=6, period=PT1H, name='1h'}":8.37E-4
                },
            "priceHistory":[],
            "lastUse":{"nano":69379300,"epochSecond":1575485763},
            "updateTimes":[1575485757810,0,0]
         },
         {
            "symbol":"VENBTC",
            "price":1.3928E-4,
            "priceChange":{"TimePeriod{id=11, period=PT24H, name='1d'}":1.35645E-4,"TimePeriod{id=10, period=PT12H, name='12h'}":1.35645E-4,"TimePeriod{id=3, period=PT5M, name='5m'}":1.35625E-4,"TimePeriod{id=11, period=PT24H, name='1d'}":1.35645E-4,"TimePeriod{id=12, period=PT72H, name='3d'}":1.35645E-4,"TimePeriod{id=4, period=PT15M, name='15m'}":1.35645E-4,"TimePeriod{id=7, period=PT4H, name='4h'}":1.35645E-4,"TimePeriod{id=14, period=PT720H, name='1M'}":1.35645E-4,"TimePeriod{id=3, period=PT5M, name='5m'}":1.35625E-4,"TimePeriod{id=2, period=PT3M, name='3m'}":1.3559E-4,"TimePeriod{id=9, period=PT8H, name='8h'}":1.35645E-4,"TimePeriod{id=5, period=PT30M, name='30m'}":1.35645E-4,"TimePeriod{id=12, period=PT72H, name='3d'}":1.35645E-4,"TimePeriod{id=8, period=PT6H, name='6h'}":1.35645E-4,"TimePeriod{id=4, period=PT15M, name='15m'}":1.35645E-4,"TimePeriod{id=13, period=PT168H, name='1w'}":1.35645E-4,"TimePeriod{id=6, period=PT1H, name='1h'}":1.35645E-4,"TimePeriod{id=13, period=PT168H, name='1w'}":1.35645E-4,"TimePeriod{id=6, period=PT1H, name='1h'}":1.35645E-4,"TimePeriod{id=8, period=PT6H, name='6h'}":1.35645E-4,"TimePeriod{id=7, period=PT4H, name='4h'}":1.35645E-4,"TimePeriod{id=5, period=PT30M, name='30m'}":1.35645E-4,"TimePeriod{id=9, period=PT8H, name='8h'}":1.35645E-4,"TimePeriod{id=2, period=PT3M, name='3m'}":1.3559E-4,"TimePeriod{id=14, period=PT720H, name='1M'}":1.35645E-4,"TimePeriod{id=10, period=PT12H, name='12h'}":1.35645E-4},"priceHistory":[],"lastUse":{"nano":70380800,"epochSecond":1575485763},"updateTimes":[1575484594972,0,0]}]}
     */

}
