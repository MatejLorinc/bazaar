package me.math3w.bazaar.menu;

import de.rapha149.signgui.SignGUI;
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
        addClickAction("back", clickInfo -> bazaarPlugin.getMenuHistory().openPrevious(clickInfo.getPlayer()));
        addClickAction("search", clickInfo -> {
            new SignGUI()
                    .lines(bazaarPlugin.getMenuConfig().getStringList("search-sign").toArray(new String[4]))
                    .onFinish((player, lines) -> {
                        String filter = lines[0];
                        bazaarPlugin.getBazaar().openSearch(player, filter);
                        return null;
                    }).open(clickInfo.getPlayer());
        });
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
