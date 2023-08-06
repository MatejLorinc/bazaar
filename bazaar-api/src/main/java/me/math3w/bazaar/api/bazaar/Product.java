package me.math3w.bazaar.api.bazaar;

import me.math3w.bazaar.api.menu.MenuInfo;
import org.bukkit.inventory.ItemStack;

public interface Product extends MenuInfo {
    ItemStack getItem();

    void setItem(ItemStack item);

    ItemStack getIcon();

    void setIcon(ItemStack icon);

    ItemStack getConfirmationIcon(double unitPrice, int amount);

    String getName();

    void setName(String name);

    String getId();

    ProductCategory getProductCategory();

    double getLowestBuyPrice();

    double getHighestSellPrice();
}
