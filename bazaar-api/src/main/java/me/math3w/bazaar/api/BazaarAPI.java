package me.math3w.bazaar.api;

import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.menu.ClickActionManager;

public interface BazaarAPI {
    Bazaar getBazaar();

    ClickActionManager getClickActionManager();
}
