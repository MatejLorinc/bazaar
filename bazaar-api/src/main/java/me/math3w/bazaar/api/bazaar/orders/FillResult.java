package me.math3w.bazaar.api.bazaar.orders;

public interface FillResult {
    int getAmount();

    double getPrice();

    void undoFill();
}
