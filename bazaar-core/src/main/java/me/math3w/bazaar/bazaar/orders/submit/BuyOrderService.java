package me.math3w.bazaar.bazaar.orders.submit;

import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.SubmitResult;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BuyOrderService implements OrderService {
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
        ItemStack item = order.getProduct().getItem();
        Player player = Bukkit.getPlayer(order.getPlayer());
        Inventory inventory = player.getInventory();

        int remainingAmount = order.getAvailableItems();

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
        return order.getAvailableItems() - remainingAmount;
    }
}
