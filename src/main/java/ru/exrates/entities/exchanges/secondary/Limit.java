package ru.exrates.entities.exchanges.secondary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class Limit {
    @Getter @Setter private String name;
            @Getter private int interval;
            @Getter private LimitType type;
    @Getter @Setter private int limit;
    @Getter @Setter private int counter;
                    private boolean accessible;

    public Limit(String name, LimitType type, int interval, int limit) {
        this.name = name;
        this.type = type;
        this.limit = limit;
        this.interval = interval;
    }

    public void count() {
        ++this.counter;
        if (counter == limit) accessible = false;
    }

    public void count(int weight){
        counter += weight;
    }

    public boolean isAccessible() {
        return accessible;
    }
}
