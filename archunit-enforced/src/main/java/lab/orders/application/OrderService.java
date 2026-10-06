package lab.orders.application;

import lab.orders.domain.Order;

public class OrderService {
    public Order place(String id, long cents) {
        return new Order(id, cents);
    }
}
