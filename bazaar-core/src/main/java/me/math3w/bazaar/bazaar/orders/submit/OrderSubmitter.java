package me.math3w.bazaar.bazaar.orders.submit;

import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.SubmitResult;

public interface OrderSubmitter {
    SubmitResult submit(BazaarOrder order);
}
