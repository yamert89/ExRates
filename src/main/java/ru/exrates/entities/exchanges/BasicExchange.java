package ru.exrates.entities.exchanges;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.exrates.configs.Properties;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;

import java.util.*;


public abstract class BasicExchange implements Exchange {
    private final static Logger logger = LogManager.getLogger(BasicExchange.class);
    private Properties props;

    @Autowired
    public void setProps(Properties props) {
        this.props = props;
    }

    @Getter
    protected TreeSet<CurrencyPair> pairs = new TreeSet<>();

    BasicExchange() {
        // init pairs
        /*logger.debug(timerPeriod);
        logger.debug(maxSize);*/
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                task();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000, props.getTimerPeriod());
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

    abstract void task();

}
