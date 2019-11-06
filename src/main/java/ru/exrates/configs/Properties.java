package ru.exrates.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class Properties {
    @Value("${app.timer}")
    private String timerPeriod; //todo min period - premium function

    @Value("${app.pairs.size}")
    private String maxSize;

    @Value("${app.pairs.strategy.persistent}")
    private String persistenceSize;

    @Value("${app.save.timer}")
    private String savingTimer;

    public int getSavingTimer(){
        return Integer.parseInt(savingTimer);
    }

    public long getTimerPeriod() {
        return Long.parseLong(timerPeriod);
    }

    public int getMaxSize() {
        return Integer.parseInt(maxSize);
    }

    public boolean isPersistenceStrategy(){
        return Boolean.parseBoolean(persistenceSize);
    }
}
