# modulith-boundaries

Two modules in one Maven project, `orders` and `inventory`, where inventory hides its implementation in an `internal` package and an ArchUnit test checks that.

## Goal

Show module boundaries inside a single deployable: other modules may use only the public API type, never the internals.

## Run it

```
mvn -q test
```

Expected: `ModuleBoundariesTest` runs 3 tests with no failures and Maven reports BUILD SUCCESS (checked here, Java 21). This uses plain ArchUnit rules, not Spring Modulith.

## What it proves

- `OrdersApi` talks to inventory only through `InventoryApi`; the real classes pass `realModulesRespectBoundaries`, which forbids outside use of `lab.inventory.internal` and `lab.orders.internal`.
- The rule bites: `src/test/java/lab/fixture/orders/Sneaky.java` references `StockInventory` directly and `rulesRejectViolatingFixture` expects an `AssertionError`.
- `modulesCollaborateThroughApiOnly` reserves 2 of 2 units of `sku-1` and then fails to reserve 1 more, so behaviour works across the API.

## Trade-offs

- The convention is a package named `internal`; a module that does not follow it is not checked.
- Wiring (`new StockInventory(...)`) happens in the test; there is no container or module descriptor here.
- Cycles between modules and event-based communication are not covered.

## When not to use it

- When the code will be split into separate services anyway and you need contract tests instead.
- If you already use Spring Modulith or Java modules, which give you this verification built in.
