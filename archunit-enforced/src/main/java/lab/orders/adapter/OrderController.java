package lab.orders.adapter;

import lab.orders.application.OrderService;
import lab.orders.domain.Order;

/** Adapter: may depend inward on application and domain. */
public class OrderController {
    private final OrderService service = new OrderService();

    public String handle(String id) {
        Order o = service.place(id, 100);
        return o.id();
    }
}
