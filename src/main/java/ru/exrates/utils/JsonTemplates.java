package ru.exrates.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class JsonTemplates {

    @NoArgsConstructor
    @Getter @Setter
    public static class ExchangePayload{
        private String exchange;
        private String[] pairs;
    }
}
