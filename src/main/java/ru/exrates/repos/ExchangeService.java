package ru.exrates.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.BasicExchange;
import ru.exrates.entities.exchanges.Exchange;
import ru.exrates.repos.daos.CurrensyRepository;
import ru.exrates.repos.daos.ExchangeRepository;


import javax.persistence.NoResultException;
import java.util.Set;

@Service
@Transactional
public class ExchangeService {

    private ExchangeRepository exchangeRepository;
    private CurrensyRepository currensyRepository;
    @Autowired
    public void setCurrensyRepository(CurrensyRepository currensyRepository) {
        this.currensyRepository = currensyRepository;
    }

    @Autowired
    public void setExchangeRepository(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    @Transactional
    @Nullable
    public BasicExchange find(int id){
        var exch = exchangeRepository.findById(id);
        return exch.orElse(null);
    }

    @Transactional
    @Nullable
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
    public Page<CurrencyPair> fillPairs(int amount){
        return currensyRepository.findAll(PageRequest.of(1, amount));
    }





}
