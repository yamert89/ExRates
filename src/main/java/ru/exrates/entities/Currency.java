package ru.exrates.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Currency {
    @Getter @Setter
    private String name;

    @Getter @Setter
    private String symbol;

    public Currency() {
    }

    public Currency(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return symbol.equals(currency.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
