package ru.exrates.entities.exchanges;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.TimePeriod;
import ru.exrates.entities.exchanges.secondary.*;

import javax.annotation.PostConstruct;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.Duration;
import java.util.*;


@Entity @DiscriminatorValue("Binance")
public class BinanceExchange extends BasicExchange {
    private final static Logger logger = LogManager.getLogger(BinanceExchange.class);

    //https://github.com/binance-exchange/binance-official-api-docs/blob/master/rest-api.md#general-api-information

    static {
        URL_ENDPOINT = "https://api.binance.com";
        URL_CURRENT_AVG_PRICE = "/api/v3/avgPrice";
        URL_INFO = "/api/v1/exchangeInfo";
        URL_PRICE_CHANGE = "";
    }

    public BinanceExchange() {
        super();
    }

    @Override
    void task () {
        if (getId() == null) return;
        logger.debug("binance task!!");
        if (!accessible()) logger.debug("Limits excess"); //Todo

        CurrencyPair pair = null;
        for (CurrencyPair p : pairs) {
            try {
                currentPrice(p);
                priceChange(p);
            } catch (JSONException e) {
                logger.error("task JS ex", e);
            } catch (LimitExceededException e){
                logger.error(e.getMessage());
                try {
                    Thread.sleep(60000); //todo add logic
                } catch (InterruptedException ex) {
                    logger.error("Interrupt ", ex);
                }
                task();
                return;
            } catch (ErrorCodeException e){
                logger.error(e.getMessage());
            } catch (BanException e){
                logger.error(e.getMessage());
            }
        }
    }

    @Override
    void currentPrice (CurrencyPair pair)
            throws JSONException, NullPointerException, LimitExceededException, ErrorCodeException, BanException {
        var entity = restTemplate.getForEntityImpl(URL_CURRENT_AVG_PRICE + "?symbol=" + pair.getSymbol(), JSONObject.class, LimitType.WEIGHT);
        pair.setPrice(Double.parseDouble(entity.getBody().getString("price")));
        count(1);
    }

    @Override
    void priceChange (CurrencyPair pair)
            throws JSONException, LimitExceededException, ErrorCodeException, BanException {
        var change = pair.getPriceChange();
        for (TimePeriod per : changePeriods) {
            var entity = restTemplate.getForEntityImpl(URL_PRICE_CHANGE, JSONArray.class, LimitType.WEIGHT);
            var array = entity.getBody().getJSONArray(0);
            change.put(per, (array.getDouble(2) + array.getDouble(3)) / 2);
        }
        count();//todo
    }
    @PostConstruct
    private void init(){
        if (getVersion() != 0) return;
        logger.debug("Postconstruct binance");
        name = "binance";
        limitCode = 429;
        banCode = 418;

        limits = new HashSet<>();
        limits.add(new Limit("MINUTE", LimitType.WEIGHT, Calendar.MINUTE, 0)); //todo limit value

        changePeriods = new LinkedList<>();
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
            var entity = restTemplate.getForEntityImpl(URL_ENDPOINT + URL_INFO, JSONObject.class, LimitType.WEIGHT).getBody();
            count(1);

            JSONArray symbols = null;
            var array = entity.getJSONArray("rateLimits");
            for (int i = 0; i < array.length(); i++) {
                var ob = array.getJSONObject(i);
                for (Limit limit : limits) {
                    var name = ob.getString("interval");
                    if (name.equals(limit.getName())) {
                        limit.setName(name);
                        limit.setLimitValue(ob.getInt("limit"));
                    }
                }

            }

            symbols = entity.getJSONArray("symbols");
            for (int i = 0; i < symbols.length(); i++) {
                pairs.add(new CurrencyPair(symbols.getJSONObject(i).getString("symbol")));
            }
        } catch (JSONException e) {
            logger.error("task JSON E", e);
        } catch (Exception e) {
            logger.error("task NPE", e);
        }
        logger.debug("exchange inited");
    }


}

    /*
    1m
3m
5m
15m
30m
1h
2h
4h
6h
8h
12h
1d
3d
1w
1M
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

