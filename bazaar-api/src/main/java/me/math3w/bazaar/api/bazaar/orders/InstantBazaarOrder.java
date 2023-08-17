package me.math3w.bazaar.api.bazaar.orders;

import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.menu.MenuInfo;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface InstantBazaarOrder extends MenuInfo {
    Product getProduct();

    int getAmount();

    double getPrice();

    OrderType getType();

    UUID getPlayer();

    ItemStack getIcon();

    int getRealAmount();

    void setRealAmount(int realAmount);

    double getRealPrice();

    void setRealPrice(double realPrice);
}
