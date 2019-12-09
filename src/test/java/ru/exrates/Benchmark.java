package ru.exrates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.exrates.entities.exchanges.secondary.RestTemplateImpl;



public class Benchmark {
    private final static Logger logger = LogManager.getLogger(Benchmark.class);

    private RestTemplateImpl restTemplate;


    @Test
    public void restTemplateTest(){
        restTemplate = new RestTemplateImpl();
        var URL_ENDPOINT = "https://api.binance.com";
        var URL_PING = "/api/v1/ping";
        long start = System.currentTimeMillis();
        var resp = restTemplate.getForEntity(URL_ENDPOINT + URL_PING, String.class).getBody();
        logger.debug(System.currentTimeMillis() - start);





    }
}
