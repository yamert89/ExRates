package ru.exrates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.exrates.entities.exchanges.secondary.RestTemplateImpl;


public class Benchmark {
    private final static Logger logger = LogManager.getLogger(Benchmark.class);

    private RestTemplateImpl restTemplate;

    private WebClient webClient;


    @Test
    public void restTemplateTest(){
        logger.debug("start rest template");
        restTemplate = new RestTemplateImpl();
        var URL_ENDPOINT = "https://api.binance.com";
        var URL_PING = "/api/v1/ping";
        long start = System.currentTimeMillis();
        var resp = restTemplate.getForEntity(URL_ENDPOINT + URL_PING, String.class).getBody();
        logger.debug(System.currentTimeMillis() - start);
        logger.debug(resp);
    }

    @Test
    public void webClientTest(){
        logger.debug("start web client");
        webClient = WebClient.create();
        var URL_ENDPOINT = "https://api.binance.com";
        var URL_PING = "/api/v1/ping";
        var URL_INFO = "/api/v1/exchangeInfo";
        long start = System.currentTimeMillis();
        var request = webClient.get().uri(URL_ENDPOINT + URL_INFO);
        WebClient.ResponseSpec retrieve = request.retrieve();
        logger.debug(System.currentTimeMillis() - start);
        String stringMono = retrieve.bodyToMono(String.class).block();
        logger.debug(stringMono);

    }
}
