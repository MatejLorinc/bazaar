package me.math3w.bazaar.bazaar.orders;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.*;
import me.math3w.bazaar.api.config.MessagePlaceholder;
import me.math3w.bazaar.bazaar.orders.submit.BuyOrderSubmitter;
import me.math3w.bazaar.bazaar.orders.submit.OrderSubmitter;
import me.math3w.bazaar.bazaar.orders.submit.SellOrderSubmitter;
import me.math3w.bazaar.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractOrderManager implements OrderManager {
    protected final BazaarPlugin bazaarPlugin;

    protected AbstractOrderManager(BazaarPlugin bazaarPlugin) {
        this.bazaarPlugin = bazaarPlugin;
    }

    @Override
    public BazaarOrder prepareBazaarOrder(Product product, int amount, double unitPrice, OrderType type, UUID player) {
        return new DefaultBazaarOrder(product, amount, BigDecimal.valueOf(unitPrice).setScale(1, RoundingMode.DOWN).doubleValue(), type, player, 0, 0, Instant.now());
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

    @Override
    public CompletableFuture<List<CompressedBazaarOrder>> getCompressedOrders(Product product, OrderType orderType, int limit) {
        return CompletableFuture.supplyAsync(() -> {
            List<CompressedBazaarOrder> compressedOrders = new ArrayList<>();

            getOrders(product, orderType, orders -> {
                int currentOrderIndex = orders.size() - 1;
                BazaarOrder currentOrder = orders.get(currentOrderIndex);

                if (compressedOrders.isEmpty()) {
                    compressedOrders.add(new DefaultCompressedBazaarOrder(currentOrder));
                    return true;
                }

                for (CompressedBazaarOrder compressedOrder : compressedOrders) {
                    if (!compressedOrder.canAddOrder(currentOrder)) continue;
                    compressedOrder.addOrder(currentOrder);
                    return true;
                }

                if (compressedOrders.size() < limit) {
                    compressedOrders.add(new DefaultCompressedBazaarOrder(currentOrder));
                    return true;
                }

                orders.remove(currentOrder);
                return false;
            }).join();

            return compressedOrders;
        });
    }
}
