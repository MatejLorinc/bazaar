package me.math3w.bazaar.config;

public final class MessagePlaceholder {
    private final String placeholder;
    private final String value;

    public MessagePlaceholder(String placeholder, String value) {
        this.placeholder = placeholder;
        this.value = value;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getValue() {
        return value;
    }
}
