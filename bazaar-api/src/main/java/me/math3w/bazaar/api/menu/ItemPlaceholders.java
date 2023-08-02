package me.math3w.bazaar.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemPlaceholders {
    void addItemPlaceholder(ItemPlaceholderFunction action);

    ItemStack replaceItemPlaceholders(ItemStack item, Player player, MenuInfo info);
}
