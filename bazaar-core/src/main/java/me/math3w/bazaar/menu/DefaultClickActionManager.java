package me.math3w.bazaar.menu;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.menu.ClickActionManager;
import me.zort.containr.ContextClickInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DefaultClickActionManager implements ClickActionManager {
    private final BazaarPlugin bazaarPlugin;
    private final Map<String, Consumer<ContextClickInfo>> clickActions = new HashMap<>();

    public DefaultClickActionManager(BazaarPlugin bazaarPlugin) {
        this.bazaarPlugin = bazaarPlugin;

        addClickAction("close", ContextClickInfo::close);
    }

    @Override
    public void addClickAction(String name, Consumer<ContextClickInfo> action) {
        clickActions.put(name, action);
    }

    @Override
    public Consumer<ContextClickInfo> getClickAction(String actionName) {
        return clickActions.getOrDefault(actionName, clickInfo -> {
        });
    }
}
