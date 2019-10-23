package ru.exrates.entities.exchanges;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.BatchSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import ru.exrates.configs.Properties;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.TimePeriod;
import ru.exrates.entities.exchanges.secondary.*;

import javax.persistence.*;
import java.util.*;

@Entity @Inheritance(strategy = InheritanceType.SINGLE_TABLE) @DiscriminatorColumn(name = "EXCHANGE_TYPE")
public abstract class BasicExchange implements Exchange {

    @Id @GeneratedValue
    @Getter
    private Integer id;
    @Getter @Setter
    @Version
    private long version;
    private final static Logger logger = LogManager.getLogger(BasicExchange.class);
    static String URL_ENDPOINT;
    static String URL_CURRENT_AVG_PRICE;
    static String URL_INFO;
    static String URL_PRICE_CHANGE;
    static String URL_PING;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @Getter
    List<TimePeriod> changePeriods;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST)
    @Getter
    Set<Limit> limits;
    int limitCode;
    int banCode;
    int sleepValueSeconds = 30;
    @Getter
    String name;
    @Getter
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST)
    protected Set<CurrencyPair> pairs = new TreeSet<>();
    @Transient
    private Properties props;
    @Transient
    RestTemplateImpl restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplateImpl restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Autowired
    public void setProps(Properties props) {
        this.props = props;
    }



    BasicExchange() {
        // init pairs


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    task();
                }catch (RuntimeException e){
                    this.cancel();
                    logger.error(e);
                    logger.debug("task cancelled because you are banned");
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 10000000/*, props.getTimerPeriod()*/); //todo value
        timer.cancel();
    }

    @Override
    public void insertPair(CurrencyPair pair){
        pairs.add(pair);
        if (pairs.size() > props.getMaxSize()) ((TreeSet) pairs).pollLast();
    }

    @Override
    public CurrencyPair getPair(Currency c1, Currency c2){
        final CurrencyPair[] pair = {null};
        pairs.spliterator().forEachRemaining(el ->  {
            if (el.getSymbol().equals(c1.getSymbol() + c2.getSymbol())) pair[0] = el;
        });
        return pair[0];
    }


    abstract void task() throws RuntimeException;

    abstract void currentPrice(CurrencyPair pair) throws
            JSONException, NullPointerException, LimitExceededException, ErrorCodeException, BanException;

    abstract void priceChange(CurrencyPair pair) throws
            JSONException, LimitExceededException, ErrorCodeException, BanException;


}
