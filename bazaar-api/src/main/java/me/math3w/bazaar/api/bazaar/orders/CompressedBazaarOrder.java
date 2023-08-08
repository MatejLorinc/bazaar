package me.math3w.bazaar.api.bazaar.orders;

import me.math3w.bazaar.api.bazaar.Product;

public interface CompressedBazaarOrder {
    boolean canAddOrder(BazaarOrder order);

    boolean addOrder(BazaarOrder order);

    Product getProduct();

    int getAmount();

    double getUnitPrice();

    OrderType getType();

    int getOrderAmount();

    BazaarOrder getSampleOrder();
}
