package ru.exrates.entities.exchanges;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.SortComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.core.annotation.Order;
import ru.exrates.configs.Properties;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.TimePeriod;
import ru.exrates.entities.exchanges.secondary.*;
import ru.exrates.entities.exchanges.secondary.exceptions.BanException;
import ru.exrates.entities.exchanges.secondary.exceptions.ErrorCodeException;
import ru.exrates.entities.exchanges.secondary.exceptions.LimitExceededException;
import ru.exrates.repos.DurationConverter;
import ru.exrates.utils.JsonSerializers;
import ru.exrates.utils.JsonTemplates;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Entity @Inheritance(strategy = InheritanceType.SINGLE_TABLE) @DiscriminatorColumn(name = "EXCHANGE_TYPE")
public abstract class BasicExchange implements Exchange {

    @Id @GeneratedValue
    @Getter
    private Integer id;
    @Getter @Setter
    boolean temporary = true;
    private final static Logger logger = LogManager.getLogger(BasicExchange.class);

    static String URL_ENDPOINT, URL_CURRENT_AVG_PRICE, URL_INFO, URL_PRICE_CHANGE, URL_PING, URL_ORDER;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Getter
    @JsonIgnore
    List<TimePeriod> changePeriods;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Getter
    @JsonIgnore
    Set<Limit> limits;

    @JsonIgnore int limitCode, banCode, sleepValueSeconds = 30;
    @JsonIgnore //TODO delete
    Duration updatePeriod;

    @Getter
    String name;


    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @Getter
    @SortComparator(CurrencyPair.CurComparator.class)
    //@org.hibernate.annotations.OrderBy(clause = "last_use desc")
    //@Column(name="last_use")
    //@OrderColumn(name = "last_use", nullable = false)
    protected final SortedSet<CurrencyPair> pairs = new TreeSet<>();

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

    }

    @PostConstruct
    protected void init(){
        logger.debug("Postconstruct " + name);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    task();
                }catch (RuntimeException e){
                    this.cancel();
                    logger.error(e);
                    logger.debug(e.getMessage());
                }
            }
        };
        Timer timer = new Timer();
        updatePeriod = Duration.ofMillis(props.getTimerPeriod());
        timer.schedule(task, 10000, props.getTimerPeriod());
        //timer.cancel();
    }

    @Override
    public void insertPair(CurrencyPair pair){
        pairs.add(pair);
        if (pairs.size() > props.getMaxSize()) pairs.remove(pairs.first()); //TODO check
    }

    @Override
    public CurrencyPair getPair(Currency c1, Currency c2){
        final CurrencyPair[] pair = {null};
        pairs.spliterator().forEachRemaining(el ->  {
            if (el.getSymbol().equals(c1.getSymbol() + c2.getSymbol())) pair[0] = el;
        });
        return pair[0];
    }

    @Override
    public CurrencyPair getPair(String pairName){
        final CurrencyPair[] p = {null};
        pairs.spliterator().forEachRemaining(el ->  {
            if (el.getSymbol().equals(pairName)) p[0] = el;
        });
        return p[0];

    }

    public boolean dataElapsed(CurrencyPair pair, Duration timeout, int idx){
        logger.debug(String.format("Pair %1$s updated on field %2$s %3$s | current time = %4$s", pair.getSymbol(), idx, Instant.ofEpochMilli(pair.getUpdateTimes()[idx]), Instant.now()));
        return Instant.now().isAfter(Instant.ofEpochMilli(pair.getUpdateTimes()[idx] + timeout.toMillis()));
    }


    protected void task() throws RuntimeException{
        logger.debug( name + " task started....");
        CurrencyPair pair = null;
        synchronized (pairs){
            for (CurrencyPair p : pairs) {
                try {
                    currentPrice(p, updatePeriod);
                    priceChange(p, updatePeriod);
                } catch (JSONException e) {
                    logger.error("task JS ex", e);
                } catch (LimitExceededException e){
                    logger.error(e.getMessage());
                    try {
                        sleepValueSeconds *= 2;
                        Thread.sleep(sleepValueSeconds);
                    } catch (InterruptedException ex) {
                        logger.error("Interrupt ", ex);
                    }
                    task();
                    return;
                } catch (ErrorCodeException e){
                    logger.error(e.getMessage());
                } catch (BanException e){
                    logger.error(e.getMessage());
                    throw new RuntimeException("You are banned from " + this.name);
                } catch (Exception e){
                    throw new RuntimeException("Unknown error", e);
                }
            }
        }

    };

    public abstract void currentPrice(CurrencyPair pair, Duration timeout) throws
            JSONException, NullPointerException, LimitExceededException, ErrorCodeException, BanException;

    public abstract void priceChange(CurrencyPair pair, Duration timeout, Map<String, String> uriVariables) throws
            JSONException, LimitExceededException, ErrorCodeException, BanException;

    public abstract void priceChange(CurrencyPair pair, Duration timeout) throws
            JSONException, LimitExceededException, ErrorCodeException, BanException;

    @Override
    public String toString() {
        return "BasicExchange{" +
                "id=" + id +
                ", temporary=" + temporary +
                ", changePeriods=" + changePeriods +
                ", limits=" + limits +
                ", limitCode=" + limitCode +
                ", banCode=" + banCode +
                ", sleepValueSeconds=" + sleepValueSeconds +
                ", updatePeriod=" + updatePeriod +
                ", name='" + name + '\'' +
                ", pairs=" + pairs +
                ", props=" + props +
                ", restTemplate=" + restTemplate +
                '}';
    }
}
