package lab.orders.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate root. All changes go through its methods, so invariants hold:
 * lines only while OPEN, quantity > 0, one currency, at most 10 lines, no empty confirm.
 */
public class Order {
    public enum Status { OPEN, CONFIRMED }

    public record Line(String sku, int qty, Money unitPrice) {
        public Line {
            if (qty <= 0) throw new IllegalArgumentException("qty must be positive");
        }

        Money subtotal() {
            return unitPrice.times(qty);
        }
    }

    private static final int MAX_LINES = 10;

    private final String id;
    private final String currency;
    private final List<Line> lines = new ArrayList<>();
    private Status status = Status.OPEN;

    public Order(String id, String currency) {
        this.id = id;
        this.currency = currency;
    }

    public void addLine(String sku, int qty, Money unitPrice) {
        if (status != Status.OPEN) throw new IllegalStateException("order is " + status);
        if (lines.size() >= MAX_LINES) throw new IllegalStateException("too many lines");
        if (!unitPrice.currency().equals(currency)) throw new IllegalArgumentException("currency mismatch");
        lines.add(new Line(sku, qty, unitPrice));
    }

    public void confirm() {
        if (lines.isEmpty()) throw new IllegalStateException("cannot confirm an empty order");
        status = Status.CONFIRMED;
    }

    public Money total() {
        return lines.stream().map(Line::subtotal).reduce(Money.zero(currency), Money::plus);
    }

    public String id() { return id; }

    public Status status() { return status; }

    /** Read-only view: callers cannot bypass the root. */
    public List<Line> lines() {
        return Collections.unmodifiableList(lines);
    }
}
