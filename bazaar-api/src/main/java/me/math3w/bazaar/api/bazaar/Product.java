package me.math3w.bazaar.api.bazaar;

import org.bukkit.inventory.ItemStack;

public interface Product {
    ItemStack getIcon();

    void setIcon(ItemStack icon);

    String getName();

    void setName(String name);

    ProductCategory getProductCategory();
}
