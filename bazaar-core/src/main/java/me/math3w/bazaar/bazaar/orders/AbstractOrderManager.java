package me.math3w.bazaar.bazaar.orders;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.OrderManager;
import me.math3w.bazaar.api.bazaar.orders.OrderType;
import me.math3w.bazaar.api.bazaar.orders.SubmitResult;
import me.math3w.bazaar.api.config.MessagePlaceholder;
import me.math3w.bazaar.bazaar.orders.submit.BuyOrderSubmitter;
import me.math3w.bazaar.bazaar.orders.submit.OrderSubmitter;
import me.math3w.bazaar.bazaar.orders.submit.SellOrderSubmitter;
import me.math3w.bazaar.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractOrderManager implements OrderManager {
    protected final BazaarPlugin bazaarPlugin;

    protected AbstractOrderManager(BazaarPlugin bazaarPlugin) {
        this.bazaarPlugin = bazaarPlugin;
    }

    @Override
    public BazaarOrder prepareBazaarOrder(Product product, int amount, double unitPrice, OrderType type, UUID player) {
        return new DefaultBazaarOrder(product, amount, unitPrice, type, player, 0, 0, Instant.now());
    }

    @Override
    public CompletableFuture<SubmitResult> submitBazaarOrder(BazaarOrder order) {
        Player player = Bukkit.getPlayer(order.getPlayer());
        OrderType type = order.getType();
        OrderSubmitter orderSubmitter = type == OrderType.BUY ? new BuyOrderSubmitter() : new SellOrderSubmitter();

        SubmitResult result = orderSubmitter.submit(order);
        bazaarPlugin.getMessagesConfig().sendMessage(player,
                result.getMessageId(type),
                new MessagePlaceholder("amount", String.valueOf(order.getAmount())),
                new MessagePlaceholder("product", order.getProduct().getName()),
                new MessagePlaceholder("coins", Utils.getTextPrice(order.getUnitPrice() * order.getAmount())));

        if (result != SubmitResult.SUCCESS) return CompletableFuture.supplyAsync(() -> result);
        return registerBazaarOrder(order);
    }

    protected abstract CompletableFuture<SubmitResult> registerBazaarOrder(BazaarOrder order);
}
