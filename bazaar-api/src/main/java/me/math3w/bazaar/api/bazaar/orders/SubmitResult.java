package me.math3w.bazaar.api.bazaar.orders;

public enum SubmitResult {
    SUCCESS,
    NOT_ENOUGH,
    ERROR;

    public String getMessageId(OrderType type) {
        return "order." + type.name().toLowerCase() + "." + name().toLowerCase().replace("_", "-");
    }
}
