package me.math3w.bazaar.bazaar.orders;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.*;
import me.math3w.bazaar.api.config.MessagePlaceholder;
import me.math3w.bazaar.bazaar.orders.submit.BuyOrderService;
import me.math3w.bazaar.bazaar.orders.submit.OrderService;
import me.math3w.bazaar.bazaar.orders.submit.SellOrderService;
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
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractOrderManager implements OrderManager {
    protected final BazaarPlugin bazaarPlugin;

    protected AbstractOrderManager(BazaarPlugin bazaarPlugin) {
        this.bazaarPlugin = bazaarPlugin;
    }

    protected static OrderService getOrderService(OrderType type) {
        return type == OrderType.BUY ? new BuyOrderService() : new SellOrderService();
    }

    @Override
    public BazaarOrder prepareBazaarOrder(Product product, int amount, double unitPrice, OrderType type, UUID player) {
        return new DefaultBazaarOrder(product, amount, BigDecimal.valueOf(unitPrice).setScale(1, RoundingMode.DOWN).doubleValue(), type, player, 0, 0, Instant.now());
    }

    @Override
    public CompletableFuture<InstantBazaarOrder> prepareInstantOrder(Product product, int amount, OrderType type, UUID player) {
        AtomicInteger amountInOrders = new AtomicInteger();
        return getOrders(product, type == OrderType.BUY ? OrderType.SELL : OrderType.BUY, orders -> {
            amountInOrders.addAndGet(orders.get(orders.size() - 1).getOrderableItems());
            return amountInOrders.get() < amount;
        }).thenApply(orders -> new DefaultInstantBazaarOrder(product, Math.min(amountInOrders.get(), amount), type, player, Utils.getTotalPrice(orders, amount)));
    }

    @Override
    public int claimOrder(BazaarOrder order) {
        Player player = Bukkit.getPlayer(order.getPlayer());
        OrderType type = order.getType();
        OrderService orderService = getOrderService(type);

        int claimed = orderService.claim(order);

        if (claimed > 0) {
            bazaarPlugin.getMessagesConfig().sendMessage(player,
                    "claim." + type.name().toLowerCase(),
                    new MessagePlaceholder("amount", String.valueOf(claimed)),
                    new MessagePlaceholder("total-coins", Utils.getTextPrice(claimed * order.getUnitPrice())),
                    new MessagePlaceholder("unit-coins", Utils.getTextPrice(order.getUnitPrice())),
                    new MessagePlaceholder("product", order.getProduct().getName()));

            registerClaim(order, claimed);
        }

        return claimed;
    }

    @Override
    public CompletableFuture<SubmitResult> submitBazaarOrder(BazaarOrder order) {
        Player player = Bukkit.getPlayer(order.getPlayer());
        OrderType type = order.getType();
        OrderService orderService = getOrderService(type);

        SubmitResult result = orderService.submit(order);
        bazaarPlugin.getMessagesConfig().sendMessage(player,
                result.getMessageId(type),
                new MessagePlaceholder("amount", String.valueOf(order.getAmount())),
                new MessagePlaceholder("product", order.getProduct().getName()),
                new MessagePlaceholder("coins", Utils.getTextPrice(order.getUnitPrice() * order.getAmount())));

        if (result != SubmitResult.SUCCESS) return CompletableFuture.supplyAsync(() -> result);
        return registerBazaarOrder(order);
    }

    @Override
    public CompletableFuture<InstantSubmitResult> submitInstantOrder(InstantBazaarOrder order) {
        Player player = Bukkit.getPlayer(order.getPlayer());
        OrderType type = order.getType();
        OrderService orderService = getOrderService(type);

        return fillOrders(order.getProduct(), type == OrderType.BUY ? OrderType.SELL : OrderType.BUY, order.getAmount()).thenApplyAsync(fillResult -> {
            int amount = fillResult.getAmount();
            double price = fillResult.getPrice();

            if (amount < order.getAmount()) {
                bazaarPlugin.getMessagesConfig().sendMessage(player, InstantSubmitResult.NOT_ENOUGH_STOCK.getMessageId(type));
                fillResult.undoFill();
                return InstantSubmitResult.NOT_ENOUGH_STOCK;
            }

            CompletableFuture<InstantSubmitResult> resultCompletableFuture = new CompletableFuture<>();

            order.setRealAmount(amount);
            order.setRealPrice(price);
            Bukkit.getScheduler().runTask(bazaarPlugin, () -> {
                InstantSubmitResult result = orderService.submit(order);
                bazaarPlugin.getMessagesConfig().sendMessage(player,
                        result.getMessageId(type),
                        new MessagePlaceholder("amount", String.valueOf(order.getRealAmount())),
                        new MessagePlaceholder("product", order.getProduct().getName()),
                        new MessagePlaceholder("coins", Utils.getTextPrice(order.getRealPrice())));
                if (result != InstantSubmitResult.SUCCESS) {
                    fillResult.undoFill();
                }
                resultCompletableFuture.complete(result);
            });

            return resultCompletableFuture.join();
        });
    }

    protected abstract CompletableFuture<SubmitResult> registerBazaarOrder(BazaarOrder order);

    protected abstract CompletableFuture<Void> registerClaim(BazaarOrder order, int claimed);

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

    @Override
    public CompletableFuture<FillResult> fillOrders(Product product, OrderType orderType, int amount) {
        AtomicInteger currentAmount = new AtomicInteger();

        return getOrders(product, orderType, orders -> currentAmount.addAndGet(orders.get(orders.size() - 1).getOrderableItems()) < amount)
                .thenApply(orders -> {
                    double price = Utils.getTotalPrice(orders, amount);

                    int remainingAmount = amount;
                    for (int i = 0; i < orders.size(); i++) {
                        if (remainingAmount <= 0) break;

                        BazaarOrder order = orders.get(i);
                        int orderFillAmount = Math.min(remainingAmount, order.getOrderableItems());

                        registerOrderFill(order, orderFillAmount).join();

                        remainingAmount -= orderFillAmount;
                    }

                    return new DefaultFillResult(this, orders, Math.min(currentAmount.get(), amount), price);
                });
    }

    protected abstract CompletableFuture<Void> registerOrderFill(BazaarOrder order, int orderFillAmount);
}
