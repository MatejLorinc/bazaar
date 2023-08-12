package me.math3w.bazaar.bazaar.orders.submit;

import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.SubmitResult;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SellOrderService implements OrderService {
    @Override
    public SubmitResult submit(BazaarOrder order) {

        Product product = order.getProduct();
        Bazaar bazaar = product.getProductCategory().getCategory().getBazaar();
        Player player = Bukkit.getPlayer(order.getPlayer());

        int playerAmount = bazaar.getProductAmountInInventory(product, player);
        if (playerAmount < order.getAmount()) {
            return SubmitResult.NOT_ENOUGH;
        }

        takeItems(player, product.getItem(), order.getAmount());

        return SubmitResult.SUCCESS;
    }

    private void takeItems(Player player, ItemStack item, int amount) {
        Inventory inventory = player.getInventory();

        int remainingAmount = amount;

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack slotItem = inventory.getItem(i);
            if (slotItem == null || !slotItem.isSimilar(item)) continue;

            int slotAmount = slotItem.getAmount();
            if (slotAmount <= remainingAmount) {
                inventory.setItem(i, null);
                remainingAmount -= slotAmount;
            } else {
                slotItem.setAmount(slotAmount - remainingAmount);
                remainingAmount = 0;
            }

            if (remainingAmount <= 0) {
                player.updateInventory();
                return;
            }
        }
    }

    @Override
    public int claim(BazaarOrder order) {
        Player player = Bukkit.getPlayer(order.getPlayer());
        Economy economy = order.getProduct().getProductCategory().getCategory().getBazaar().getBazaarApi().getEconomy();
        double availableCoins = order.getAvailableCoins();

        economy.depositPlayer(player, availableCoins);

        return order.getAvailableItems();
    }
}
