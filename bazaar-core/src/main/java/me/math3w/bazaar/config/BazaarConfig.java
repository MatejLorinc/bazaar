package me.math3w.bazaar.config;

import me.math3w.bazaar.bazaar.category.CategoryConfiguration;
import me.math3w.bazaar.bazaar.productcategory.ProductCategoryConfiguration;
import me.math3w.bazaar.utils.Utils;
import me.zort.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BazaarConfig extends CustomConfig {
    public BazaarConfig(JavaPlugin plugin) {
        super(plugin, "bazaar");
    }

    @Override
    protected void addDefaults() {
        addDefaultCategories();
    }

    private void addDefaultCategories() {
        ArrayList<CategoryConfiguration> categories = new ArrayList<>();

        categories.add(createDefaultCategory(Material.GOLD_HOE, "&eFarming", new ArrayList<>()));
        categories.add(createDefaultCategory(Material.DIAMOND_PICKAXE, "&bMining", new ArrayList<>()));
        categories.add(createDefaultCategory(Material.IRON_SWORD, "&cCombat", new ArrayList<>()));
        categories.add(createDefaultCategory(Material.FISHING_ROD, "&6Woods & Fishes", new ArrayList<>()));
        categories.add(createDefaultCategory(Material.ENCHANTMENT_TABLE, "&dOddities", new ArrayList<>()));

        addDefault("categories", categories);
    }

    private CategoryConfiguration createDefaultCategory(Material icon, String name, List<ProductCategoryConfiguration> productCategories) {
        return new CategoryConfiguration(ItemBuilder.newBuilder(icon)
                .withName(Utils.colorize(name))
                .appendLore(ChatColor.DARK_GRAY + "Category", "", ChatColor.YELLOW + "Click to view!")
                .build(),
                name,
                productCategories);
    }

    public List<CategoryConfiguration> getCategories() {
        return (List<CategoryConfiguration>) getConfig().getList("categories", new ArrayList<>());
    }
}
