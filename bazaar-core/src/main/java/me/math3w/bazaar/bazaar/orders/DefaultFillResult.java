package me.math3w.bazaar.bazaar.orders;

import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.FillResult;
import me.math3w.bazaar.api.bazaar.orders.OrderManager;

import java.util.List;

public class DefaultFillResult implements FillResult {
    private final OrderManager orderManager;
    private final List<BazaarOrder> previousOrders;
    private final int amount;
    private final double price;

    public DefaultFillResult(OrderManager orderManager, List<BazaarOrder> previousOrders, int amount, double price) {
        this.orderManager = orderManager;
        this.previousOrders = previousOrders;
        this.amount = amount;
        this.price = price;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void undoFill() {
        for (BazaarOrder previousOrder : previousOrders) {
            orderManager.undoOrderFill(previousOrder);
        }
    }
}
