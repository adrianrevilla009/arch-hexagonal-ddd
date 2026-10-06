# arch-hexagonal-ddd

Six small Java projects that show hexagonal architecture and domain-driven design building blocks, with architecture rules enforced by tests so the boundaries cannot erode silently.

## What is inside

| Folder | What it shows | Run |
| --- | --- | --- |
| [`ports-adapters`](./ports-adapters) | Inbound and outbound ports, a CLI driving adapter, an in-memory driven adapter | `mvn -q test` |
| [`aggregates-value-objects`](./aggregates-value-objects) | `Order` aggregate root and `Money` value object protecting invariants | `mvn -q test` |
| [`domain-events`](./domain-events) | An aggregate that records events and a small in-process publisher | `mvn -q test` |
| [`anti-corruption-layer`](./anti-corruption-layer) | Translating a legacy order record into a clean domain model | `mvn -q test` |
| [`archunit-enforced`](./archunit-enforced) | ArchUnit rules: framework-free domain and inward-pointing layers | `mvn -q test` |
| [`modulith-boundaries`](./modulith-boundaries) | Two modules that may only talk through public APIs, checked with ArchUnit | `mvn -q test` |

## Prerequisites

- Java 21
- Maven 3.8 or newer (each folder has its own `pom.xml`; run commands from inside the folder)

## How to read it

Start with `ports-adapters` for the basic shape, then `archunit-enforced` to see how the rules are checked. The other folders are independent and can be read in any order. They share a tiny Orders domain.
