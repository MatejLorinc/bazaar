package me.math3w.bazaar.bazaar;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.bazaar.Category;
import me.math3w.bazaar.bazaar.category.CategoryImpl;
import me.math3w.bazaar.menu.BazaarMenu;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class BazaarImpl implements Bazaar {
    private final BazaarPlugin bazaarPlugin;
    private final List<Category> categories;
    private final BazaarMenu menu;

    public BazaarImpl(BazaarPlugin bazaarPlugin) {
        this.bazaarPlugin = bazaarPlugin;
        this.categories = bazaarPlugin.getBazaarConfig().getCategories().stream()
                .map(categoryConfiguration -> new CategoryImpl(this, categoryConfiguration))
                .collect(Collectors.toList());
        menu = new BazaarMenu(this);
    }

    @Override
    public void open(Player player) {
        menu.getMenu(categories.get(0)).open(player);
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public void saveConfig() {
        bazaarPlugin.getBazaarConfig().save();
    }
}
