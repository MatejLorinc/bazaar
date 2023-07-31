package me.math3w.bazaar.bazaar;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.bazaar.Category;
import me.math3w.bazaar.bazaar.category.CategoryImpl;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class BazaarImpl implements Bazaar {
    private final BazaarPlugin bazaarPlugin;
    private final List<Category> categories;

    public BazaarImpl(BazaarPlugin bazaarPlugin) {
        this.bazaarPlugin = bazaarPlugin;
        this.categories = bazaarPlugin.getBazaarConfig().getCategories().stream()
                .map(categoryConfiguration -> new CategoryImpl(this, categoryConfiguration))
                .collect(Collectors.toList());
    }

    @Override
    public void open(Player player) {
        open(player, categories.get(0));
    }

    @Override
    public void open(Player player, Category category) {
        category.getMenu().open(player);
    }

    @Override
    public void openSearch(Player player, String filter) {
        bazaarPlugin.getBazaarConfig().getSearchMenuConfiguration().getMenu(this, filter).open(player);
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public void saveConfig() {
        bazaarPlugin.getBazaarConfig().save();
    }

    @Override
    public BazaarAPI getBazaarApi() {
        return bazaarPlugin;
    }
}
