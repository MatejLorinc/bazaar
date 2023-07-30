package me.math3w.bazaar.api.config;

import org.bukkit.inventory.ItemStack;

public interface MenuConfig {
    ItemStack replaceLorePlaceholders(ItemStack icon, String placeholder, MessagePlaceholder... innerPlaceholders);
}
