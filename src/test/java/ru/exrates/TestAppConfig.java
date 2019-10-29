package ru.exrates;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@TestConfiguration
@EnableJpaRepositories(basePackages = "ru.exrates.repos")
@EnableTransactionManagement
public class TestAppConfig {

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
