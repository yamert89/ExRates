package ru.exrates.entities.exchanges;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;

import java.util.Map;
import java.util.Queue;
import java.util.Set;

@Component
@PropertySource("classpath:application.properties")
public class BinanceExchange extends BasicExchange {
    private final static Logger logger = LogManager.getLogger(BinanceExchange.class);
    //https://github.com/binance-exchange/binance-official-api-docs/blob/master/rest-api.md#general-api-information



    public BinanceExchange() {
        super();
        pairs.add(new CurrencyPair(new Currency("USD"), new Currency("BTC")));

    }

    @Override
    void task() {
        logger.debug("binance task!!");
    }
}
