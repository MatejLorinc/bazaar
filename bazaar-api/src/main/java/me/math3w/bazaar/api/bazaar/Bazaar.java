package me.math3w.bazaar.api.bazaar;

import me.math3w.bazaar.api.BazaarAPI;
import org.bukkit.entity.Player;

import java.util.List;

public interface Bazaar {
    void open(Player player);

    void open(Player player, Category category);

    void openSearch(Player player, String filter);

    List<Category> getCategories();

    void saveConfig();

    BazaarAPI getBazaarApi();
}
