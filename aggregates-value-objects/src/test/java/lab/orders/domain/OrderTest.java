package lab.orders.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OrderTest {
    private final Money tenEur = new Money(1000, "EUR");

    @Test
    void totalSumsLines() {
        Order o = new Order("o1", "EUR");
        o.addLine("a", 2, tenEur);
        o.addLine("b", 1, new Money(500, "EUR"));
        assertEquals(new Money(2500, "EUR"), o.total());
    }

    @Test
    void valueObjectsEqualByValue() {
        assertEquals(new Money(1, "EUR"), new Money(1, "EUR"));
        assertThrows(IllegalArgumentException.class, () -> new Money(-1, "EUR"));
    }

    @Test
    void cannotConfirmEmptyOrder() {
        assertThrows(IllegalStateException.class, () -> new Order("o1", "EUR").confirm());
    }

    @Test
    void confirmedOrderIsFrozen() {
        Order o = new Order("o1", "EUR");
        o.addLine("a", 1, tenEur);
        o.confirm();
        assertThrows(IllegalStateException.class, () -> o.addLine("b", 1, tenEur));
    }

    @Test
    void rejectsForeignCurrencyAndBadQty() {
        Order o = new Order("o1", "EUR");
        assertThrows(IllegalArgumentException.class, () -> o.addLine("a", 1, new Money(1, "USD")));
        assertThrows(IllegalArgumentException.class, () -> o.addLine("a", 0, tenEur));
    }

    @Test
    void linesViewIsReadOnly() {
        Order o = new Order("o1", "EUR");
        assertThrows(UnsupportedOperationException.class, () -> o.lines().add(new Order.Line("x", 1, tenEur)));
    }

    @Test
    void lineLimitEnforced() {
        Order o = new Order("o1", "EUR");
        for (int i = 0; i < 10; i++) o.addLine("s" + i, 1, tenEur);
        assertThrows(IllegalStateException.class, () -> o.addLine("extra", 1, tenEur));
    }
}
