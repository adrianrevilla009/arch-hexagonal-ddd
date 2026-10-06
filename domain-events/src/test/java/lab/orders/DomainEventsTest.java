package lab.orders;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import lab.orders.application.EventPublisher;
import lab.orders.domain.DomainEvent;
import lab.orders.domain.DomainEvent.OrderCancelled;
import lab.orders.domain.DomainEvent.OrderPlaced;
import lab.orders.domain.Order;
import org.junit.jupiter.api.Test;

class DomainEventsTest {
    private final Clock clock = Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);

    @Test
    void aggregateRecordsEventsAndPullClearsThem() {
        Order order = Order.place("o1", 900, clock);
        order.cancel();
        List<DomainEvent> events = order.pullEvents();
        assertEquals(2, events.size());
        assertInstanceOf(OrderPlaced.class, events.get(0));
        assertInstanceOf(OrderCancelled.class, events.get(1));
        assertTrue(order.pullEvents().isEmpty());
    }

    @Test
    void handlersReceiveOnlyTheirEventType() {
        EventPublisher publisher = new EventPublisher();
        List<String> seen = new ArrayList<>();
        publisher.subscribe(OrderPlaced.class, e -> seen.add("placed:" + e.orderId()));
        publisher.subscribe(OrderCancelled.class, e -> seen.add("cancelled:" + e.orderId()));

        Order order = Order.place("o1", 900, clock);
        order.cancel();
        publisher.publishAll(order.pullEvents());

        assertEquals(List.of("placed:o1", "cancelled:o1"), seen);
    }

    @Test
    void doubleCancelFailsAndEmitsNothingExtra() {
        Order order = Order.place("o1", 1, clock);
        order.cancel();
        assertThrows(IllegalStateException.class, order::cancel);
        assertEquals(2, order.pullEvents().size());
    }
}
