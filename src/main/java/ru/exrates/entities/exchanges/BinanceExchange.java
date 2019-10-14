package ru.exrates.entities.exchanges;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Component
public class BinanceExchange extends BasicExchange {
    private final static Logger logger = LogManager.getLogger(BinanceExchange.class);
    private final String endpoint = "https://api.binance.com";
    //https://github.com/binance-exchange/binance-official-api-docs/blob/master/rest-api.md#general-api-information

    private RestTemplate restTemplate;
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }






    public BinanceExchange() {
        super();
        pairs.add(new CurrencyPair(new Currency("USD"), new Currency("BTC")));

    }

    @Override
    void task() {
        logger.debug("binance task!!");
        ResponseEntity<JSONObject> entity = restTemplate.getForEntity(endpoint + "/api/v1/exchangeInfo", JSONObject.class);

    }
}
