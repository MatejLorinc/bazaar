package me.math3w.bazaar.api.bazaar;

import me.math3w.bazaar.api.menu.MenuInfo;
import me.zort.containr.GUI;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ProductCategory extends MenuInfo {
    ItemStack getIcon();

    void setIcon(ItemStack icon);

    String getName();

    void setName(String name);

    List<Product> getProducts();

    GUI getMenu();

    Category getCategory();
}
