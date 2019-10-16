package ru.exrates.entities.exchanges.secondary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class Limit {
    @Getter @Setter private String name;
            @Getter private int type;
    @Getter @Setter private int limit;
    @Getter @Setter private int counter;
                    private boolean accessible;

    public Limit(String name, int type, int limit) {
        this.name = name;
        this.type = type;
        this.limit = limit;
    }

    public void count() {
        ++this.counter;
        if (counter == limit) accessible = false;
    }

    public boolean isAccessible() {
        return accessible;
    }
}
