package ru.exrates.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class Properties {
    @Value("${timer}")
    private String timerPeriod; //todo min period - premium function

    @Value("${pairs.size}")
    private String maxSize;

    public long getTimerPeriod() {
        return Long.parseLong(timerPeriod);
    }

    public int getMaxSize() {
        return Integer.parseInt(maxSize);
    }
}
