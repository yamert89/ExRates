package ru.exrates.entities.exchanges.secondary.collections;

import ru.exrates.entities.CurrencyPair;
import ru.exrates.entities.exchanges.BasicExchange;

import java.time.Instant;
import java.util.HashMap;

public class UpdateListenerMap<K, V> extends HashMap<K, V> {
    private CurrencyPair pair;

    public UpdateListenerMap(CurrencyPair pair) {
        super();
        this.pair = pair;
    }

    @Override
    public V put(K key, V value) {
        pair.getUpdateTimes()[1] = Instant.now().toEpochMilli();
        return super.put(key, value);
    }

    @Override
    public V remove(Object key) {
        pair.getUpdateTimes()[1] = Instant.now().toEpochMilli();
        return super.remove(key);
    }
}
