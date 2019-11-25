package ru.exrates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class ExRatesApplication {

    public static void main(String[] args) {
        try {
            System.out.println("start");
            SpringApplication.run(ExRatesApplication.class, args);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
