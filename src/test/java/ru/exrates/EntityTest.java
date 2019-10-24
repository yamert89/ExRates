package ru.exrates;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import ru.exrates.entities.exchanges.BinanceExchange;
import ru.exrates.func.Aggregator;
import ru.exrates.repos.ExchangeService;
import ru.exrates.repos.daos.ExchangeRepository;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
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
        Assert.assertEquals(14, exchange.getChangePeriods().size());
        Assert.assertNotEquals(0, size);
        System.out.println(size);
    }
}
