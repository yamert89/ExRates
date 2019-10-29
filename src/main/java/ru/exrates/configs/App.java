package ru.exrates.configs;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.BasicExchange;
import ru.exrates.entities.exchanges.BinanceExchange;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "ru.exrates.repos")
@EnableTransactionManagement
public class App {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){ return builder.build();}

    @Bean @Lazy
    public BinanceExchange binanceExchange(){
        return new BinanceExchange();
    }

    @Bean
    @ConfigurationProperties("app.datasource.first")
    public DataSourceProperties firstDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.second")
    public DataSourceProperties secondDataSourceProperties(){
        return new DataSourceProperties();
    }










    
}
