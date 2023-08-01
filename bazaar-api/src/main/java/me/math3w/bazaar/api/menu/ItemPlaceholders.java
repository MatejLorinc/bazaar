package me.math3w.bazaar.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public interface ItemPlaceholders {
    void addItemPlaceholder(BiFunction<ItemStack, Player, ItemStack> action);

    ItemStack replaceItemPlaceholders(ItemStack item, Player player);
}
