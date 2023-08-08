package me.math3w.bazaar.api.menu;

import me.zort.containr.ContainerComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ItemPlaceholderFunction {
    ItemStack apply(ContainerComponent containerComponent, ItemStack item, int itemSlot, Player player, MenuInfo info);
}
