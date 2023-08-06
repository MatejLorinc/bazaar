package me.math3w.bazaar.api;

import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.bazaar.orders.OrderManager;
import me.math3w.bazaar.api.config.MenuConfig;
import me.math3w.bazaar.api.menu.ClickActionManager;
import me.math3w.bazaar.api.menu.ItemPlaceholders;
import net.milkbowl.vault.economy.Economy;

public interface BazaarAPI {
    Economy getEconomy();

    MenuConfig getMenuConfig();

    Bazaar getBazaar();

    ClickActionManager getClickActionManager();

    ItemPlaceholders getItemPlaceholders();

    OrderManager getOrderManager();
}
