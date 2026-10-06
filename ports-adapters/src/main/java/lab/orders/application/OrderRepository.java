package lab.orders.application;

import java.util.Optional;
import java.util.UUID;
import lab.orders.domain.Order;

/** Driven (outbound) port. The application owns the interface; adapters implement it. */
public interface OrderRepository {
    void save(Order order);

    Optional<Order> findById(UUID id);
}
