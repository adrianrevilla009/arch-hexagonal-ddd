# domain-events

An `Order` aggregate that records `OrderPlaced` and `OrderCancelled` events, and a synchronous `EventPublisher` that delivers them to typed handlers.

## Goal

Show how an aggregate can announce what happened without calling other components, and how the application layer dispatches those facts afterwards.

## Run it

```
mvn -q test
```

Expected: `DomainEventsTest` runs 3 tests with no failures and Maven reports BUILD SUCCESS (checked here, Java 21).

## What it proves

- `Order.place` and `Order.cancel` append events to a pending list; `pullEvents()` hands them over in order and empties the list, so a second pull is empty.
- `EventPublisher.subscribe(Class, handler)` calls a handler only for events of its own type; the test sees `placed:o1` then `cancelled:o1`.
- A second `cancel()` throws `IllegalStateException` and adds no extra event. Events carry time from an injected `Clock`, which the test fixes, and `DomainEvent` is a sealed interface in `domain/`.

## Trade-offs

- Dispatch is synchronous and in memory: a failing handler stops the loop, and nothing is persisted or retried.
- Events are lost if the caller never pulls them; there is no outbox here.
- The aggregate holds only an id and a cancelled flag, so it is smaller than a real order.

## When not to use it

- When a single method call to a known collaborator is enough and nobody else needs to react.
- When events must survive crashes or reach other services; use an outbox and a broker instead.
