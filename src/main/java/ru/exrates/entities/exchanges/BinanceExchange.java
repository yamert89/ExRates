package ru.exrates.entities.exchanges;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

@Component
public class BinanceExchange extends BasicExchange {
    private final static Logger logger = LogManager.getLogger(BinanceExchange.class);


    //https://github.com/binance-exchange/binance-official-api-docs/blob/master/rest-api.md#general-api-information

    private RestTemplate restTemplate;


    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    static {
        URL_ENDPOINT = "https://api.binance.com";
        URL_CURRENT_AVG_PRICE = "/api/v3/avgPrice";
        URL_INFO = "/api/v1/exchangeInfo";
        URL_PRICE_CHANGE = "";
    }






    public BinanceExchange() {
        super();
        changeVolume = new String[]{"3m", "5m", "15m", "30m", "1h", "2h", "4h", "6h", "8h", "12h", "8h", "1d", "3d", "1w", "1M"};
        var entity = restTemplate.getForEntity(URL_ENDPOINT + URL_INFO, JSONObject.class);
        JSONArray symbols = null;
        try {
            symbols = entity.getBody().getJSONArray("symbols");
            for (int i = 0; i < symbols.length(); i++) {
                pairs.add(new CurrencyPair(symbols.getJSONObject(i).getString("symbol")));
            }
        } catch (JSONException e) {
            logger.error("task JSON E", e);
        } catch (Exception e){
            logger.error("task NPE", e);
        }
        logger.debug("exchange inited");

    }

    @Override
    void task() {
        logger.debug("binance task!!");


    }

    @Override
    void currentPrice(CurrencyPair pair) throws JSONException, NullPointerException{
        var entity = restTemplate.getForEntity(URL_CURRENT_AVG_PRICE + "?symbol=" + pair.getSymbol(), JSONObject.class);
        pair.setPrice(Double.parseDouble(entity.getBody().getString("price")));
    }

    @Override
    void priceChange(CurrencyPair pair) throws JSONException {
        Stream.of(changeVolume).forEach(e -> {
                   /* var entity = restTemplate.getForEntity(
                }*/
            //
        });
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
}
