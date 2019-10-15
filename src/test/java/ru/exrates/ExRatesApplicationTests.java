package ru.exrates;

import static org.junit.Assert.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.exrates.entities.Currency;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.BasicExchange;
import ru.exrates.entities.exchanges.BinanceExchange;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//@EnableWebMvc
public class ExRatesApplicationTests {
    private final static Logger logger = LogManager.getLogger(ExRatesApplicationTests.class);

    @Test
    public void contextLoads() {

        BasicExchange exchange = mock(BasicExchange.class);
        Currency c1 = new Currency();
        c1.setName("BTC");
        Currency c2 = new Currency();
        c2.setName("USD");
        var map = new HashMap<String, Double>();
        map.put("1d", 34.54);
        var pair1 = new CurrencyPair(c1, c2);
        var pair2 = new CurrencyPair(c2, c1);
        var pair3 = new CurrencyPair(c1, c2);
        var pair4 = new CurrencyPair(c1, c2);
        try {
            pair2.getSymbol();
            Thread.sleep(100);
            pair1.getSymbol();
            Thread.sleep(100);
            pair4.getSymbol();
            Thread.sleep(100);
            pair3.getSymbol();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        var pairs = new TreeSet<CurrencyPair>();
        pairs.add(pair1);
        pairs.add(pair2);
        pairs.add(pair3);
        pairs.add(pair4);


        /*when(exchange.currentPrice(c1, c2)).thenReturn(1.99);
        when(exchange.priceChange(c1,c2)).thenReturn(map);*/
        when(exchange.getPairs()).thenReturn(pairs);

        pairs.pollFirst();

        assertEquals(pair1, exchange.getPairs().first());
        assertEquals(pair3, exchange.getPairs().last());

    }

    @Test
    public void restTemplate(){
        RestTemplate template = new RestTemplate();
        String url = "https://api.binance.com/api/v1/exchangeInfo";
        ResponseEntity entity = template.getForEntity(url, String.class);
        logger.debug(entity.toString());
        assertEquals(200, entity.getStatusCodeValue());
    }

    @Test
    public void simpleFunc(){

    }
    @Test
    public static void main2(String[] args) {
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date(1570320000000L));
        System.out.println(new Date(1570406400000L));
        System.out.println(new Date(1570492800000L));
    }



}
