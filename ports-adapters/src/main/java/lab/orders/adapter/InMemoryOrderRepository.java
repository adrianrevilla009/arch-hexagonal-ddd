package lab.orders.adapter;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lab.orders.application.OrderRepository;
import lab.orders.domain.Order;

/** Driven adapter: swap for JDBC/JPA without touching the application. */
public class InMemoryOrderRepository implements OrderRepository {
    private final Map<UUID, Order> store = new ConcurrentHashMap<>();

    @Override
    public void save(Order order) {
        store.put(order.id(), order);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }
}
