package ru.exrates.entities.exchanges.secondary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Limits")
public class Limit {
    @Id
    @GeneratedValue
    private Integer id;
    @Getter @Setter private String name;
            @Column(name = "_interval")
            @Getter private int interval;
            @Enumerated(value = EnumType.STRING)
            @Column(length = 20)
            @Getter private LimitType type;
    @Getter @Setter private int limitValue;
    @Getter @Setter private int counter;
                    @Column(name = "_accessible")
                    private boolean accessible;

    public Limit(String name, LimitType type, int interval, int limit) {
        this.name = name;
        this.type = type;
        this.limitValue = limit;
        this.interval = interval;
    }

    public void count() {
        ++this.counter;
        if (counter == limitValue) accessible = false;
    }

    public void count(int weight){
        counter += weight;
    }

    public boolean isAccessible() {
        return accessible;
    }
}
