package ru.exrates.entities.exchanges;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import ru.exrates.configs.Properties;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.secondary.*;

import java.util.*;


public abstract class BasicExchange implements Exchange {
    private final static Logger logger = LogManager.getLogger(BasicExchange.class);
    private Properties props;
    String[] changeVolume;
    Set<Limit> limits;
    int errorCode;
    static String URL_ENDPOINT;
    static String URL_CURRENT_AVG_PRICE;
    static String URL_INFO;
    static String URL_PRICE_CHANGE;

    RestTemplateImpl restTemplate;


    @Autowired
    public void setRestTemplate(RestTemplateImpl restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Autowired
    public void setProps(Properties props) {
        this.props = props;
    }

    @Getter
    protected TreeSet<CurrencyPair> pairs = new TreeSet<>();

    BasicExchange() {
        // init pairs

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                task();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000/*, props.getTimerPeriod()*/);
    }

    @Override
    public void insertPair(CurrencyPair pair){
        pairs.add(pair);
        if (pairs.size() > props.getMaxSize()) pairs.pollLast();
    }

    @Override
    public CurrencyPair getPair(Currency c1, Currency c2){
        final CurrencyPair[] pair = {null};
        pairs.spliterator().forEachRemaining(el ->  {
            if (el.getSymbol().equals(c1.getSymbol() + c2.getSymbol())) pair[0] = el;
        });
        return pair[0];
    }

    boolean accessible(){
        for (Limit limit : limits) {
            if (!limit.isAccessible()) return false;
        }
        return true;
    }

    void count(){
        for (Limit limit : limits) {
            if (limit.getType() == LimitType.REQUEST) limit.count();
        }
    }

    void count(int weight) {
        for (Limit limit : limits) {
            if (limit.getType() == LimitType.WEIGHT) limit.count(weight);
        }
    }


    abstract void task();

    abstract void currentPrice(CurrencyPair pair) throws
            JSONException, NullPointerException, LimitExceededException, ErrorCodeException;

    abstract void priceChange(CurrencyPair pair) throws
            JSONException, LimitExceededException, ErrorCodeException;

}
