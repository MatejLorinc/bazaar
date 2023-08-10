package me.math3w.bazaar.api.config;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface MenuConfig {
    List<String> getStringList(String path, Placeholder... placeholders);

    String getString(String path, MessagePlaceholder... placeholders);

    ItemStack replaceLorePlaceholders(ItemStack icon, String placeholder, Placeholder... innerPlaceholders);
}
