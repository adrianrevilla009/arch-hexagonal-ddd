# aggregates-value-objects

An `Order` aggregate root with order lines and a `Money` value object, plus tests for each invariant.

## Goal

Make invalid orders impossible to build or modify: all rules live in the domain classes, not in callers.

## Run it

```
mvn -q test
```

Expected: `OrderTest` runs 7 tests with no failures and Maven reports BUILD SUCCESS (checked here, Java 21).

## What it proves

- `Money` (a record in `Money.java`) rejects negative cents and a currency that is not three characters, and two equal amounts compare equal; adding or multiplying keeps the currency, and adding different currencies throws.
- `Order.java` allows lines only while the status is OPEN, requires quantity above zero, one currency per order, at most 10 lines, and refuses to confirm an empty order.
- `lines()` returns an unmodifiable view, so a caller cannot add a line behind the root's back (`linesViewIsReadOnly`).

## Trade-offs

- The invariants hold only if every change goes through the root; a persistence mapper would need a rebuild constructor, which this folder does not have.
- Records keep value objects short but expose their components; `Money` uses `long` cents and a plain string currency, not a currency type.
- Order status only goes from OPEN to CONFIRMED; there is no cancellation or other transition.

## When not to use it

- For plain data holders or reporting read models with no real rules, an anemic record is simpler and honest.
- When validation depends on other aggregates or external data; that cannot live inside one aggregate.
