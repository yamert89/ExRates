package ru.exrates.entities.exchanges;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.TimePeriod;
import ru.exrates.entities.exchanges.secondary.*;
import ru.exrates.entities.exchanges.secondary.exceptions.BanException;
import ru.exrates.entities.exchanges.secondary.exceptions.ErrorCodeException;
import ru.exrates.entities.exchanges.secondary.exceptions.LimitExceededException;
import ru.exrates.repos.DurationConverter;

import javax.annotation.PostConstruct;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
@Entity @DiscriminatorValue("Binance")
public class BinanceExchange extends BasicExchange {
    private final static Logger logger = LogManager.getLogger(BinanceExchange.class);

    //https://github.com/binance-exchange/binance-official-api-docs/blob/master/rest-api.md#general-api-information

    static {
        URL_ENDPOINT = "https://api.binance.com";
        URL_CURRENT_AVG_PRICE = "/api/v3/avgPrice";
        URL_INFO = "/api/v1/exchangeInfo";
        URL_PRICE_CHANGE = "/api/v1/klines";
        URL_PING = "/api/v1/ping";
    }

    public BinanceExchange() {
        super();
    }

    @Override
    void task () throws RuntimeException{
        if (getId() == null) return;
        var resp = restTemplate.getForEntity(URL_ENDPOINT + URL_PING, String.class).getStatusCode().value();
        if (resp != 200) return;
        logger.debug("binance task!!");
        CurrencyPair pair = null;
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
            }
        }
    }

    @Override
    public void currentPrice (CurrencyPair pair, Duration timeout)
            throws JSONException, NullPointerException, LimitExceededException, ErrorCodeException, BanException { //todo timeout
        if (!dataElapsed(pair, timeout)) return;
        var entity = new JSONObject(restTemplate.getForEntityImpl(URL_CURRENT_AVG_PRICE + "?symbol=" + pair.getSymbol(), String.class, LimitType.WEIGHT).getBody());
        pair.setPrice(Double.parseDouble(entity.getString("price")));

    }

    @Override
    public void priceChange (CurrencyPair pair, Duration timeout)
            throws JSONException, LimitExceededException, ErrorCodeException, BanException {
        if (!dataElapsed(pair, timeout)) return;
        var change = pair.getPriceChange();
        var symbol = "?symbol=" + pair.getSymbol();
        var period = "&interval=";
        for (TimePeriod per : changePeriods) {
            var entity = new JSONArray(restTemplate.getForEntityImpl(URL_PRICE_CHANGE +
                    symbol + period + per.getName() + "&limit=1" , String.class, LimitType.WEIGHT).getBody());
            var array = entity.getJSONArray(0);
            change.put(per, (array.getDouble(2) + array.getDouble(3)) / 2);
        }
    }

    @Override
    public void priceChange (CurrencyPair pair, Duration timeout, Map<String, String> uriVariables) //todo limit > 1 logic
            throws JSONException, LimitExceededException, ErrorCodeException, BanException{
        if (!dataElapsed(pair, timeout)) return;
        var change = pair.getPriceChange();
        for (TimePeriod per : changePeriods) {
            var entity = new JSONArray(restTemplate.getForEntityImpl(
                    URL_PRICE_CHANGE, String.class, uriVariables, LimitType.WEIGHT).getBody());
            var array = entity.getJSONArray(0);
            change.put(per, (array.getDouble(2) + array.getDouble(3)) / 2);
        }
    }

    @PostConstruct
    private void init(){
        logger.debug("Postconstruct binance");
        if (getVersion() != 0) return;
        name = "binance";
        limitCode = 429;
        banCode = 418;

        limits = new HashSet<>();


        changePeriods = new ArrayList<>();
        Collections.addAll(changePeriods,
                new TimePeriod(Duration.ofMinutes(3), "3m"),
                new TimePeriod(Duration.ofMinutes(5), "5m"),
                new TimePeriod(Duration.ofMinutes(15), "15m"),
                new TimePeriod(Duration.ofMinutes(30), "30m"),
                new TimePeriod(Duration.ofHours(1), "1h"),
                new TimePeriod(Duration.ofHours(4), "4h"),
                new TimePeriod(Duration.ofHours(6), "6h"),
                new TimePeriod(Duration.ofHours(8), "8h"),
                new TimePeriod(Duration.ofHours(12), "12h"),
                new TimePeriod(Duration.ofDays(1), "1d"),
                new TimePeriod(Duration.ofDays(3), "3d"),
                new TimePeriod(Duration.ofDays(7), "1w"),
                new TimePeriod(Duration.ofDays(30), "1M"));

        restTemplate.setLimitCode(limitCode);
        restTemplate.setBanCode(banCode);
        try {
            var entity = new JSONObject(restTemplate.getForEntityImpl(URL_ENDPOINT + URL_INFO, String.class, LimitType.WEIGHT)
                    .getBody());




            JSONArray symbols = null;
            var array = entity.getJSONArray("rateLimits");

            limits.add(new Limit("MINUTE", LimitType.WEIGHT, Duration.ofMinutes(1), 1200));

            for (int i = 0; i < array.length(); i++) {
                var ob = array.getJSONObject(i);
                for (Limit limit : limits) {
                    var name = ob.getString("interval");
                    if (name.equals(limit.getName())) {
                        limit.setLimitValue(ob.getInt("limit"));
                    }
                }

            }

            symbols = entity.getJSONArray("symbols");
            for (int i = 0; i < symbols.length(); i++) {
                pairs.add(new CurrencyPair(symbols.getJSONObject(i).getString("symbol")));
            }
            logger.debug("exchange initialized with " + pairs.size() + " pairs");
        } catch (JSONException e) {
            logger.error("task JSON E", e);
        } catch (LimitExceededException e) {
            logger.error(e.getMessage());
        } catch (BanException e){
            logger.error(e.getMessage());
        } catch (NullPointerException e){
            logger.error("NPE init");
        } catch (Exception e){
            logger.error("Unknown exc in init", e);
        }


    }


}

/*
JSON
 */


    /*
    {
  "timezone": "UTC",
  "serverTime": 1565246363776,
  "rateLimits": [
    {
      //These are defined in the `ENUM definitions` section under `Rate Limiters (rateLimitType)`.
      //All limits are optional
    }
  ],
  "exchangeFilters": [
    //These are the defined filters in the `Filters` section.
    //All filters are optional.
  ],
  "symbols": [
    {
      "symbol": "ETHBTC",
      "status": "TRADING",
      "baseAsset": "ETH",
      "baseAssetPrecision": 8,
      "quoteAsset": "BTC",
      "quotePrecision": 8,
      "orderTypes": [
        "LIMIT",
        "LIMIT_MAKER",
        "MARKET",
        "STOP_LOSS",
        "STOP_LOSS_LIMIT",
        "TAKE_PROFIT",
        "TAKE_PROFIT_LIMIT"
      ],
      "icebergAllowed": true,
      "ocoAllowed": true,
      "isSpotTradingAllowed": true,
      "isMarginTradingAllowed": false,
      "filters": [
        //These are defined in the Filters section.
        //All filters are optional
      ]
    }
  ]
}
     */

