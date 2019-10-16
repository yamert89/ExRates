package ru.exrates.entities.exchanges.secondary;

public class LimitExceededException extends Exception{
    private LimitType type;

    public LimitExceededException(LimitType type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return String.format("%1$s %2$s %3$s", "Limit <", type, "> exceeded");
    }


}
