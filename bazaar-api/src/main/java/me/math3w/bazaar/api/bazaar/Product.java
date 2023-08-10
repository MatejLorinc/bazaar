package me.math3w.bazaar.api.bazaar;

import me.math3w.bazaar.api.menu.MenuInfo;
import me.zort.containr.ContainerComponent;
import me.zort.containr.internal.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

public interface Product extends MenuInfo {
    ItemStack getItem();

    void setItem(ItemStack item);


    ItemStack getIcon(ContainerComponent container, int itemSlot, Player player);

    void setIcon(ItemStack icon);

    ItemStack getConfirmationIcon(double unitPrice, int amount);

    String getName();

    void setName(String name);

    String getId();

    ProductCategory getProductCategory();

    CompletableFuture<Double> getLowestBuyPrice();

    CompletableFuture<Double> getHighestSellPrice();

    CompletableFuture<Pair<Double, Integer>> getBuyPriceWithOrderableAmount(int amount);

    CompletableFuture<Pair<Double, Integer>> getSellPriceWithOrderableAmount(int amount);
}
