package me.math3w.bazaar.api.bazaar.orders;

import me.math3w.bazaar.api.bazaar.Product;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public interface OrderManager {
    BazaarOrder prepareBazaarOrder(Product product, int amount, double unitPrice, OrderType type, UUID player);

    CompletableFuture<SubmitResult> submitBazaarOrder(BazaarOrder order);

    CompletableFuture<List<BazaarOrder>> getOrders(Product product, OrderType type, Predicate<List<BazaarOrder>> shouldContinuePredicate);

    CompletableFuture<List<CompressedBazaarOrder>> getCompressedOrders(Product product, OrderType orderType, int limit);
}
