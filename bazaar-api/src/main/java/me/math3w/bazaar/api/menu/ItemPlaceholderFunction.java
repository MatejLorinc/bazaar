package me.math3w.bazaar.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ItemPlaceholderFunction {
    ItemStack apply(ItemStack item, Player player, MenuInfo info);
}
