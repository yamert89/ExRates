package ru.exrates.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.BasicExchange;
import ru.exrates.entities.exchanges.Exchange;
import ru.exrates.repos.daos.ExchangeRepository;


import javax.persistence.NoResultException;
import java.util.Set;

@Service
@Transactional
public class ExchangeService {

    private ExchangeRepository exchangeRepository;
    @Autowired
    public void setExchangeRepository(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    @Transactional
    public BasicExchange find(String name){
        try {
            return exchangeRepository.findByName(name);
        }catch (NoResultException e){
            return null;
        }
    }

    @Transactional
    public BasicExchange persist(BasicExchange exchange){
        return exchangeRepository.save(exchange);
    }

    @Transactional
    public Set<CurrencyPair> fillPairs(int amount){
        return exchangeRepository.fillpairs(amount);
    }



}
