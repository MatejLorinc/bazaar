package me.math3w.bazaar.api.bazaar;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Category {
    ItemStack getIcon();

    void setIcon(ItemStack icon);

    String getName();

    void setName(String name);

    List<ProductCategory> getProductCategories();

    Bazaar getBazaar();
}