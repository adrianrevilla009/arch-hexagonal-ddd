package lab.orders.domain;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

/** Aggregate that records events instead of calling collaborators directly. */
public class Order {
    private final String id;
    private final Clock clock;
    private boolean cancelled;
    private final List<DomainEvent> pending = new ArrayList<>();

    private Order(String id, Clock clock) {
        this.id = id;
        this.clock = clock;
    }

    public static Order place(String id, long totalCents, Clock clock) {
        Order order = new Order(id, clock);
        order.pending.add(new DomainEvent.OrderPlaced(id, totalCents, clock.instant()));
        return order;
    }

    public void cancel() {
        if (cancelled) throw new IllegalStateException("already cancelled");
        cancelled = true;
        pending.add(new DomainEvent.OrderCancelled(id, clock.instant()));
    }

    /** Hands the recorded events to the caller and clears them. */
    public List<DomainEvent> pullEvents() {
        List<DomainEvent> out = List.copyOf(pending);
        pending.clear();
        return out;
    }
}
