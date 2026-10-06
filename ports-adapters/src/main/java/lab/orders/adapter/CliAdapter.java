package lab.orders.adapter;

import java.util.UUID;
import lab.orders.application.OrderService;
import lab.orders.application.PlaceOrder;

/** Driving adapter: translates command-line args into a call on the inbound port. */
public class CliAdapter {
    public static void main(String[] args) {
        InMemoryOrderRepository repo = new InMemoryOrderRepository();
        PlaceOrder placeOrder = new OrderService(repo);
        UUID id = placeOrder.place(args.length > 0 ? args[0] : "alice", 1500);
        System.out.println("placed " + id + " -> " + repo.findById(id).orElseThrow());
    }
}
