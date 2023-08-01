package me.math3w.bazaar.api;

import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.config.MenuConfig;
import me.math3w.bazaar.api.menu.ClickActionManager;
import me.math3w.bazaar.api.menu.ItemPlaceholders;

public interface BazaarAPI {
    MenuConfig getMenuConfig();

    Bazaar getBazaar();

    ClickActionManager getClickActionManager();

    ItemPlaceholders getItemPlaceholders();
}
