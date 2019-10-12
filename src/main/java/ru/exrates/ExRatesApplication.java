package ru.exrates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:/app.properties")
public class ExRatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExRatesApplication.class, args);
    }

}
