package me.math3w.bazaar.bazaar.orders;

import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.OrderType;

import java.time.Instant;
import java.util.UUID;

public class DefaultBazaarOrder implements BazaarOrder {
    private final Product product;
    private final int amount;
    private final double unitPrice;
    private final OrderType type;
    private final UUID player;
    private final Instant createdAt;
    private int filled;
    private int claimed;

    public DefaultBazaarOrder(Product product, int amount, double unitPrice, OrderType type, UUID player, int filled, int claimed, Instant createdAt) {
        this.product = product;
        this.amount = amount;
        this.unitPrice = unitPrice;
        this.type = type;
        this.player = player;
        this.filled = filled;
        this.claimed = claimed;
        this.createdAt = createdAt;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public OrderType getType() {
        return type;
    }

    @Override
    public UUID getPlayer() {
        return player;
    }

    @Override
    public int getFilled() {
        return filled;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public void fill(int amount) {
        this.filled += amount;
    }

    @Override
    public int getClaimed() {
        return claimed;
    }

    @Override
    public void claim(int amount) {
        this.claimed += amount;
    }

    @Override
    public boolean isSimilar(BazaarOrder other) {
        if (!product.equals(other.getProduct())) return false;
        if (unitPrice != other.getUnitPrice()) return false;
        if (type != other.getType()) return false;
        return true;
    }
}
