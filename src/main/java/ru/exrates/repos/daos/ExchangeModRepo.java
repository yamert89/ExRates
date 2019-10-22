package ru.exrates.repos.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.BasicExchange;
import ru.exrates.entities.exchanges.Exchange;

import javax.persistence.NoResultException;
import java.util.Set;

@NoRepositoryBean
public interface ExchangeModRepo extends JpaRepository<BasicExchange, Integer> {
    BasicExchange findByName(String name) throws NoResultException;
    Set<CurrencyPair> fillpairs(int amount);
}
