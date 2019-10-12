package ru.exrates.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.exrates.entities.exchanges.Exchange;

@RestController
public class RestInfo {

    @GetMapping("/rest/exchange")
    public Exchange getExchange(@RequestParam String exchange){

    }

}
