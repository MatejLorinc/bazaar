package me.math3w.bazaar.bazaar.orders.submit;

import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.InstantBazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.InstantSubmitResult;
import me.math3w.bazaar.api.bazaar.orders.SubmitResult;

public interface OrderService {
    InstantSubmitResult submit(InstantBazaarOrder order);

    SubmitResult submit(BazaarOrder order);

    int claim(BazaarOrder order);

    int claim(InstantBazaarOrder order);
}
