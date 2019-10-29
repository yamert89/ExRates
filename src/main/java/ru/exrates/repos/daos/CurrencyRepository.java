package ru.exrates.repos.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.exrates.entities.CurrencyPair;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<CurrencyPair, Integer> {

    Optional<CurrencyPair> findBySymbol(String symbol);

}
