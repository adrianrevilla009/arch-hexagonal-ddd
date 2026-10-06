package lab.orders.domain;

import java.util.UUID;

/** Domain entity: no framework imports. */
public record Order(UUID id, String customer, long totalCents) {
    public Order {
        if (totalCents < 0) throw new IllegalArgumentException("total must be >= 0");
    }
}
