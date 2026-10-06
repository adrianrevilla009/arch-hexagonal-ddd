# archunit-enforced

A small three-layer Orders codebase (`domain`, `application`, `adapter`) and an ArchUnit test that fails the build if the dependency rules are broken.

## Goal

Turn the architecture rules into tests: the domain uses no framework code and dependencies only point inward.

## Run it

```
mvn -q test
```

Expected: `ArchitectureTest` passes (the two `@ArchTest` rules plus `rulesRejectViolatingFixture`), 3 tests in total, BUILD SUCCESS (checked here, Java 21, ArchUnit 1.3.0).

## What it proves

- `domainIsFrameworkFree`: classes in `..domain..` may depend only on `domain`, `java.lang`, `java.util` and `java.time`.
- `layersPointInward`: nothing may access `adapter`; `application` is accessed only by `adapter`; `domain` only by `application` and `adapter`.
- The rules really fail when violated: `src/test/java/lab/fixture/domain/BadEntity.java` holds a `javax.swing.JButton`, and the test asserts the rule throws `AssertionError` on it. The fixture is in test sources, so the main rules do not scan it.

## Trade-offs

- Rules are package-name based; code placed in the wrong package escapes them.
- The allowed-package list for the domain is strict and must be extended by hand for things like `java.math`.
- Only a few rules are shown; real projects need more (naming, cycles, annotations).

## When not to use it

- In a very small or short-lived codebase where code review is enough.
- When the project already uses a module system or build-level modules that enforce the same boundaries.
