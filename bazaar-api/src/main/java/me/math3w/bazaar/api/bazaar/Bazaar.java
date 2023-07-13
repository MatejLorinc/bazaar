package me.math3w.bazaar.api.bazaar;

import org.bukkit.entity.Player;

import java.util.List;

public interface Bazaar {
    void open(Player player);

    List<Category> getCategories();

    void saveConfig();
}
