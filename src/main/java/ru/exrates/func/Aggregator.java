package ru.exrates.func;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.BasicExchange;
import ru.exrates.entities.exchanges.BinanceExchange;
import ru.exrates.entities.exchanges.Exchange;
import ru.exrates.entities.exchanges.secondary.Limit;
import ru.exrates.entities.exchanges.secondary.exceptions.BanException;
import ru.exrates.entities.exchanges.secondary.exceptions.ErrorCodeException;
import ru.exrates.entities.exchanges.secondary.exceptions.LimitExceededException;
import ru.exrates.repos.ExchangeService;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.IntStream;

@Component
public class Aggregator {
    private final static Logger logger = LogManager.getLogger(Aggregator.class);
    private Map<String, BasicExchange> exchanges;
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
        logger.debug("Postconstruct aggregator init");
        logger.debug(exchangeService);
        for (var set : exchangeNames.entrySet()) {
            BasicExchange exchange = exchangeService.find(set.getKey());
            int pairsSize = 0;
            if (exchange == null) {
                try {
                    exchange = applicationContext.getBean(set.getValue());
                    /*Class claz = Class.forName(set.getValue().getCanonicalName());
                    var ob = claz.getConstructor().newInstance();
                    exchange = set.getValue().cast(ob);*/
                    exchange = exchangeService.persist(exchange);
                    exchanges.put(set.getKey(), exchange);
                    pairsSize = calculatePairsSize(exchange);
                    var pairs = new TreeSet<>(exchange.getPairs());
                    while (pairs.size() > pairsSize) pairs.pollLast();
                    exchange.getPairs().clear();
                    Collections.addAll(exchange.getPairs(), pairs.toArray(new CurrencyPair[]{}));
                } catch (Exception e) {
                    logger.error("Exchange initialize crashed", e);
                }
            }else {
                pairsSize = calculatePairsSize(exchange);
                exchangeService.fillPairs(pairsSize);
            }
        }
    }

    //private Set<Currency> currencies = new HashSet<>();

    public BasicExchange getExchange(String exName){
        return exchanges.get(exName);
    } //todo needs update?

    public Exchange getExchange(String exchange, String[] pairsN, String period) {
        var exch = getExchange(exchange);
        var pairs = exch.getPairs();
        var temp = new CurrencyPair();
        for (String s : pairsN) {
            temp.setSymbol(s);
            if (!pairs.contains(temp)) exch.insertPair(exchangeService.findPair(s));
        }

        var reqPairs = new HashSet<>(pairs);

        //todo - limit request pairs

        var timePeriod = exch.getChangePeriods().stream().filter(
                p -> p.getName().equals(period)).findFirst().orElseThrow();
        for (CurrencyPair reqPair : reqPairs) {
            try {
                exch.currentPrice(reqPair, timePeriod.getPeriod());
                exch.priceChange(reqPair, timePeriod.getPeriod());
            } catch (JSONException e){
                logger.error("Json ex");
            } catch (LimitExceededException e) {
                logger.error(e.getMessage());
            } catch (ErrorCodeException e) {
                logger.error(e.getMessage());
            } catch (BanException e) {
                logger.error(e.getMessage());
            } catch (NoSuchElementException e){
                logger.error(e);
            } catch (Exception e){
                logger.error("unchecked exc", e);
            }

        }
        return exch;
    }

    public Set<CurrencyPair> getCurStat(String curName1, String curName2){
        var tempCur = new Currency(curName1);
        var tempCur2 = new Currency(curName2);
        var curs = new HashSet<CurrencyPair>();
        exchanges.forEach((key, val) -> curs.add(val.getPair(tempCur, tempCur2)));
        return curs;
    }

      //todo check for seconds limit
    public int calculatePairsSize(BasicExchange exchange){
        var tLimits = new LinkedList<Integer>();
        var ammountReqs = exchange.getChangePeriods().size() + 1;
        for (Limit limit : exchange.getLimits()) {
            var l = (int)((limit.getLimitValue() / (double)(limit.getInterval().getSeconds() / 60)) / ammountReqs);
            logger.debug("tLimit = " + l);
            tLimits.add(l);
        }
        int counter = 0;
        for (Integer tLimit : tLimits) {
            counter += tLimit;
        }
        return counter / tLimits.size();
    }



}
