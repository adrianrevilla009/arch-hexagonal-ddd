package lab.orders.application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lab.orders.domain.DomainEvent;

/** Tiny synchronous in-process dispatcher. Handlers are registered per event type. */
public class EventPublisher {
    private record Subscription<T extends DomainEvent>(Class<T> type, Consumer<T> handler) {
        void deliver(DomainEvent e) {
            if (type.isInstance(e)) handler.accept(type.cast(e));
        }
    }

    private final List<Subscription<?>> subscriptions = new ArrayList<>();

    public <T extends DomainEvent> void subscribe(Class<T> type, Consumer<T> handler) {
        subscriptions.add(new Subscription<>(type, handler));
    }

    public void publishAll(List<DomainEvent> events) {
        for (DomainEvent e : events) subscriptions.forEach(s -> s.deliver(e));
    }
}
