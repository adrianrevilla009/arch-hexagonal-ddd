# anti-corruption-layer

A legacy order record with cryptic fields, a clean `Order` domain model, a translator between them, and an adapter behind an outbound port.

## Goal

Keep a legacy system's naming, status codes and amount format out of the domain by translating at one boundary.

## Run it

```
mvn -q test
```

Expected: `AntiCorruptionLayerTest` runs 4 tests with no failures and Maven reports BUILD SUCCESS (checked here, Java 21). The legacy client is a plain `Map`, not a real system.

## What it proves

- `LegacyOrderTranslator` turns `LegacyOrderRecord(" A-1 ", "C9", "S", "12.50", "eur")` into `Order("A-1", "C9", SHIPPED, 1250, "EUR")`: it trims, maps codes N/S/X to PENDING/SHIPPED/CANCELLED, converts the decimal string to cents and upper-cases the currency.
- An unknown status code throws `IllegalArgumentException`, and an amount like `1.005` throws `ArithmeticException` instead of being rounded.
- `LegacyOrdersAdapter` implements the `LegacyOrders` port and returns only domain `Order` values or an empty `Optional`; `lab.legacy` types never leave the adapter.

## Trade-offs

- The translator must be updated whenever the legacy format changes, and it is the one place that knows both models.
- Strict rejection of bad data surfaces problems early but can block reads of legacy records that would otherwise be usable.
- Only reading by id is modelled; writes back to the legacy side are not.

## When not to use it

- When the upstream model is already close to yours and you control it.
- For a one-off data migration, where a throwaway script is enough.
