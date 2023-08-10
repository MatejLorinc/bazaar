package me.math3w.bazaar.api.config;

public final class MessagePlaceholder implements Placeholder {
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

    public String replace(String text) {
        return text.replaceAll("%" + placeholder + "%", value);
    }
}
