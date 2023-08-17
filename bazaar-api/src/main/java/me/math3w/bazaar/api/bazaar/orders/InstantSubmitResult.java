package me.math3w.bazaar.api.bazaar.orders;

public enum InstantSubmitResult {
    SUCCESS,
    NOT_ENOUGH,
    NOT_ENOUGH_STOCK,
    ERROR;

    public String getMessageId(OrderType type) {
        return "instant." + type.name().toLowerCase() + "." + name().toLowerCase().replace("_", "-");
    }
}
