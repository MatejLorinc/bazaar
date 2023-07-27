package me.math3w.bazaar.api.menu;

import me.zort.containr.ContextClickInfo;

import java.util.function.Consumer;

public interface ClickActionManager {
    void addClickAction(String name, Consumer<ContextClickInfo> action);

    Consumer<ContextClickInfo> getClickAction(String actionName);
}
