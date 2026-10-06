# ports-adapters

A hexagonal Orders slice: one inbound port, one outbound port, a CLI adapter, an in-memory repository and a unit test with a fake repository.

## Goal

Show how the application owns its ports, so the use case runs with any adapter on either side and the domain stays free of infrastructure.

## Run it

```
mvn -q test
```

Expected: `OrderServiceTest` runs 2 tests with no failures and Maven reports BUILD SUCCESS (checked here, Java 21).

`CliAdapter.main` places a 1500-cent order for the customer in the first argument (default `alice`) and prints the generated id. Not run end to end here; the tests are the verified path.

## What it proves

- `OrderService` depends only on the `OrderRepository` and `PlaceOrder` interfaces in `application/`; the test passes a `FakeRepo` and needs no real adapter.
- `InMemoryOrderRepository` implements the port from the `adapter` package, so replacing it with JDBC would not touch `OrderService`.
- The `Order` record in `domain/` rejects a negative total, which the test `negativeTotalRejected` observes through the use case.

## Trade-offs

- Every capability needs an interface plus an implementation; for a tiny service this is extra files.
- The domain `Order` here is a plain record with one rule; it does not show richer aggregate behaviour (see `aggregates-value-objects`).
- Nothing in this folder enforces the dependency direction; that is done in `archunit-enforced`.

## When not to use it

- For a throwaway script or a CRUD service with one storage backend, direct calls are simpler.
- When there is only ever one driving and one driven technology and no need to test the use case in isolation.
