package lab.orders.domain;

/** Value object: immutable, equal by value, validates itself on construction. */
public record Money(long cents, String currency) {
    public Money {
        if (cents < 0) throw new IllegalArgumentException("money cannot be negative");
        if (currency == null || currency.length() != 3) throw new IllegalArgumentException("ISO currency required");
    }

    public static Money zero(String currency) {
        return new Money(0, currency);
    }

    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(cents + other.cents, currency);
    }

    public Money times(int qty) {
        return new Money(cents * qty, currency);
    }

    private void requireSameCurrency(Money other) {
        if (!currency.equals(other.currency)) throw new IllegalArgumentException("currency mismatch");
    }
}
