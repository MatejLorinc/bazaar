package me.math3w.bazaar.bazaar.orders.submit;

import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.SubmitResult;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BuyOrderSubmitter implements OrderSubmitter {
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
}
