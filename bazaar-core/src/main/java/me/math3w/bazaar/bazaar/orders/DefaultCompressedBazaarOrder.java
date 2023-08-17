package me.math3w.bazaar.bazaar.orders;

import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.CompressedBazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.OrderType;

import java.util.HashSet;
import java.util.Set;

public class DefaultCompressedBazaarOrder implements CompressedBazaarOrder {
    private final Set<BazaarOrder> orders;

    public DefaultCompressedBazaarOrder(Set<BazaarOrder> orders) {
        this.orders = orders;
    }

    public DefaultCompressedBazaarOrder(BazaarOrder initialOrder) {
        this(new HashSet<>());
        this.orders.add(initialOrder);
    }

    @Override
    public boolean canAddOrder(BazaarOrder order) {
        return order.isSimilar(getSampleOrder());
    }

    @Override
    public boolean addOrder(BazaarOrder order) {
        if (!canAddOrder(order)) return false;
        orders.add(order);
        return true;
    }

    @Override
    public Product getProduct() {
        return getSampleOrder().getProduct();
    }

    @Override
    public int getAmount() {
        return orders.stream().mapToInt(BazaarOrder::getOrderableItems).sum();
    }

    @Override
    public double getUnitPrice() {
        return getSampleOrder().getUnitPrice();
    }

    @Override
    public OrderType getType() {
        return getSampleOrder().getType();
    }

    @Override
    public int getOrderAmount() {
        return orders.size();
    }

    @Override
    public BazaarOrder getSampleOrder() {
        return orders.iterator().next();
    }
}
