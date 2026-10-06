package lab.orders.domain;

import java.time.Instant;

/** Facts that happened, named in the past tense. */
public sealed interface DomainEvent {
    Instant occurredAt();

    record OrderPlaced(String orderId, long totalCents, Instant occurredAt) implements DomainEvent {}

    record OrderCancelled(String orderId, Instant occurredAt) implements DomainEvent {}
}
