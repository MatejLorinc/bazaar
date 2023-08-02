package me.math3w.bazaar.api.bazaar;

import me.math3w.bazaar.api.menu.MenuInfo;
import org.bukkit.inventory.ItemStack;

public interface Product extends MenuInfo {
    ItemStack getItem();

    void setItem(ItemStack item);

    ItemStack getIcon();

    void setIcon(ItemStack icon);

    String getName();

    void setName(String name);

    ProductCategory getProductCategory();

    double getLowestBuyPrice();

    double getHighestSellPrice();
}
