package ru.exrates.entities.exchanges.secondary;

public class ErrorCodeException extends Exception{
    @Override
    public String getMessage() {
        return "Error code not defined";
    }
}
