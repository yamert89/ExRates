package ru.exrates.repos.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.exrates.entities.CurrencyPair;

public interface CurrensyRepository extends JpaRepository<CurrencyPair, Integer> {
}
