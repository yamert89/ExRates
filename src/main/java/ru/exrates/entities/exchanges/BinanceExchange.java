package ru.exrates.entities.exchanges;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.secondary.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.stream.Stream;

@Component
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
        limitCode = 429;
        banCode = 418;
        restTemplate.setLimitCode(limitCode);
        restTemplate.setBanCode(banCode);
        limits = new HashSet<>();
        limits.add(new Limit("MINUTE", LimitType.WEIGHT, Calendar.MINUTE, 0));

        changeVolume = new String[]{"3m", "5m", "15m", "30m", "1h", "2h", "4h", "6h", "8h", "12h", "8h", "1d", "3d", "1w", "1M"};


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
                        limit.setLimit(ob.getInt("limit"));
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

        @Override
        void task () {
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
                        Thread.sleep(60000);
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
            for (String s : changeVolume) {
                var entity = restTemplate.getForEntityImpl(URL_PRICE_CHANGE, JSONArray.class, LimitType.WEIGHT);
                var array = entity.getBody().getJSONArray(0);
                change.put(s, (array.getDouble(2) + array.getDouble(3)) / 2);
            }
            count();//todo
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

