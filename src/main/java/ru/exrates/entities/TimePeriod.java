package ru.exrates.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.exrates.repos.DurationConverter;

import javax.persistence.*;
import java.time.Duration;
import java.time.Period;


@Entity @Table(name = "change_periods")
@NoArgsConstructor
public class TimePeriod {
    @Id
    @GeneratedValue
    @Getter
    private Integer id;

    @Getter @Setter
    @Column(nullable = false) @Convert(converter = DurationConverter.class)
    private Duration period;

    @Getter @Setter
    @Column(nullable = false, unique = true)
    private String name;

    public TimePeriod(Duration period, String name) {
        this.period = period;
        this.name = name;

    }
}
