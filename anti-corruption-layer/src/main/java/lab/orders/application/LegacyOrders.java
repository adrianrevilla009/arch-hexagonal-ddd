package lab.orders.application;

import java.util.Optional;
import lab.orders.domain.Order;

/** Outbound port, expressed in our language only. */
public interface LegacyOrders {
    Optional<Order> findById(String id);
}
