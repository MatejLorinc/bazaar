package me.math3w.bazaar.bazaar.orders.submit;

import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.InstantBazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.InstantSubmitResult;
import me.math3w.bazaar.api.bazaar.orders.SubmitResult;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BuyOrderService implements OrderService {

    @Override
    public InstantSubmitResult submit(InstantBazaarOrder order) {
        Product product = order.getProduct();
        Economy economy = product.getProductCategory().getCategory().getBazaar().getBazaarApi().getEconomy();
        Player player = Bukkit.getPlayer(order.getPlayer());
        double price = order.getRealPrice();

        if (!economy.has(player, price)) {
            return InstantSubmitResult.NOT_ENOUGH;
        }

        economy.withdrawPlayer(player, price);
        claim(order);
        return InstantSubmitResult.SUCCESS;
    }

    @Override
    public SubmitResult submit(BazaarOrder order) {
        Product product = order.getProduct();
        Economy economy = product.getProductCategory().getCategory().getBazaar().getBazaarApi().getEconomy();
        Player player = Bukkit.getPlayer(order.getPlayer());
        double totalPrice = order.getUnitPrice() * order.getAmount();

        if (!economy.has(player, totalPrice)) {
            return SubmitResult.NOT_ENOUGH;
        }

        economy.withdrawPlayer(player, totalPrice);
        return SubmitResult.SUCCESS;
    }

    @Override
    public int claim(BazaarOrder order) {
        return claim(Bukkit.getPlayer(order.getPlayer()), order.getProduct().getItem(), order.getAvailableItems());
    }

    @Override
    public int claim(InstantBazaarOrder order) {
        return claim(Bukkit.getPlayer(order.getPlayer()), order.getProduct().getItem(), order.getRealAmount());
    }

    private int claim(Player player, ItemStack item, int amount) {
        Inventory inventory = player.getInventory();

        int remainingAmount = amount;

        if (remainingAmount == 0) return 0;

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack slotItem = inventory.getItem(i);
            if (slotItem != null && !slotItem.isSimilar(item)) continue;

            int slotAvailableAmount = item.getMaxStackSize() - (slotItem == null ? 0 : slotItem.getAmount());
            if (slotAvailableAmount <= 0) continue;

            if (slotItem == null) {
                inventory.setItem(i, item);
            }

            slotItem = inventory.getItem(i);

            if (slotAvailableAmount >= remainingAmount) {
                slotItem.setAmount(slotAvailableAmount == item.getMaxStackSize() ? remainingAmount : slotItem.getAmount() + remainingAmount);
                remainingAmount = 0;
            } else {
                slotItem.setAmount(item.getMaxStackSize());
                remainingAmount -= slotAvailableAmount;
            }

            if (remainingAmount == 0) {
                player.updateInventory();
                break;
            }
        }

        return amount - remainingAmount;
    }
}
