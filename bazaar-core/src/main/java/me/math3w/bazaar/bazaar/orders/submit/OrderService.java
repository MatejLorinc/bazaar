package me.math3w.bazaar.bazaar.orders.submit;

import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.SubmitResult;

public interface OrderService {
    SubmitResult submit(BazaarOrder order);

    int claim(BazaarOrder order);
}
