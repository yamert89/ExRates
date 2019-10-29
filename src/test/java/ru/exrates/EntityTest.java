package ru.exrates;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import ru.exrates.configs.App;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.BinanceExchange;
import ru.exrates.func.Aggregator;
import ru.exrates.repos.ExchangeService;
import ru.exrates.repos.daos.ExchangeRepository;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackageClasses = {Aggregator.class, ExchangeService.class})
public class EntityTest {

    @Autowired
    ExchangeService exchangeService;

    @Autowired
    Aggregator aggregator;

    @Test
    public void calculatePairsSizeTest(){
        BinanceExchange exchange = (BinanceExchange) exchangeService.find(1);
        int size = aggregator.calculatePairsSize(exchange);
        assertEquals(14, exchange.getChangePeriods().size());
        assertNotEquals(0, size);
        System.out.println(size);
    }

    @Test
    public void dataElapsed(){
        var exch = new BinanceExchange();
        var pair = new CurrencyPair("df");
        pair.setPrice(1);
        boolean elapsed = exch.dataElapsed(pair, Duration.ofSeconds(1));
        assertFalse(elapsed);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        elapsed = exch.dataElapsed(pair, Duration.ofSeconds(1));
        assertTrue(elapsed);
    }
}
