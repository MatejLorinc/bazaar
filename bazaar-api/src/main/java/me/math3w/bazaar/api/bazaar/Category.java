package me.math3w.bazaar.api.bazaar;

import me.math3w.bazaar.api.menu.MenuInfo;
import me.zort.containr.GUI;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Category extends MenuInfo {
    ItemStack getIcon();

    void setIcon(ItemStack icon);

    String getName();

    void setName(String name);

    List<ProductCategory> getProductCategories();

    void addProductCategory(ProductCategory productCategory);

    void removeProductCategory(ProductCategory productCategory);

    GUI getMenu();

    void setTitle(String name);

    GUI getEditMenu();

    Bazaar getBazaar();
}
