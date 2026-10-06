package lab.orders.application;

import java.util.UUID;
import lab.orders.domain.Order;

/** Use case implementation; depends only on ports. */
public class OrderService implements PlaceOrder {
    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public UUID place(String customer, long totalCents) {
        Order order = new Order(UUID.randomUUID(), customer, totalCents);
        repository.save(order);
        return order.id();
    }
}
