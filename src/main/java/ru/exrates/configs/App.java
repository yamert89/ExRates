package ru.exrates.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import ru.exrates.entities.CurrencyPair;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "ru.exrates.repos")
@EnableTransactionManagement
public class App {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){ return builder.build();}

    /*@Bean
    @ConfigurationProperties("app.datasource")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }*/


    
}
