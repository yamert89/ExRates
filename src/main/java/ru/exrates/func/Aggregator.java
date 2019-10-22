package ru.exrates.func;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.BasicExchange;
import ru.exrates.entities.exchanges.BinanceExchange;
import ru.exrates.entities.exchanges.Exchange;
import ru.exrates.repos.ExchangeService;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class Aggregator {
    private final static Logger logger = LogManager.getLogger(Aggregator.class);
    private Map<String, Exchange> exchanges;
    private Map<String, Class<? extends BasicExchange>> exchangeNames = new HashMap<>();
    private ExchangeService exchangeService;
    private ApplicationContext applicationContext;

    {
        exchangeNames.put("binance", BinanceExchange.class);
    }
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setExchangeService(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    public Aggregator() {
        exchanges = new HashMap<>();
    }

    @PostConstruct
    private void init(){
        logger.debug(exchangeService);
        for (var set : exchangeNames.entrySet()) {
            BasicExchange exchange = exchangeService.find(set.getKey());
            if (exchange == null) {
                try {
                    exchange = applicationContext.getBean(set.getValue());
                    /*Class claz = Class.forName(set.getValue().getCanonicalName());
                    var ob = claz.getConstructor().newInstance();
                    exchange = set.getValue().cast(ob);*/
                    exchange = exchangeService.persist(exchange);
                    exchanges.put(set.getKey(), exchange); //todo keep only top pairs

                } catch (Exception e) {
                    logger.error("Exchange initialize crashed", e);
                }
            }else {
                exchange.getLimits().forEach(limit -> {
                    limit.getInterval().getSeconds() / limit.getLimitValue();
                        }
                exchangeService.fillPairs(
            }
        }
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
