package me.math3w.bazaar.api.menu;

import me.zort.containr.ContextClickInfo;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ClickActionManager {
    void addClickAction(String name, Consumer<ContextClickInfo> action);

    void addClickAction(String name, Function<MenuInfo, Consumer<ContextClickInfo>> action);

    Consumer<ContextClickInfo> getClickAction(String actionName, MenuInfo menuInfo);
}
