package ru.exrates.configs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.exrates.controllers.RestInfo;

import javax.annotation.PreDestroy;
import javax.persistence.PostLoad;

public class LifeCircle {
    private final static Logger logger = LogManager.getLogger(LifeCircle.class);
    @PreDestroy
    public void destroy(){
        logger.trace("Exrates destroyed ...");
    }

    @PostLoad
    public void load(){
        logger.trace("Exrates start ... ");
    }
}
