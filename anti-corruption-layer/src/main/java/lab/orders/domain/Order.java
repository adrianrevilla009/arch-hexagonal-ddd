package lab.orders.domain;

/** Our clean model. It knows nothing about the legacy format. */
public record Order(String id, String customerId, Status status, long totalCents, String currency) {
    public enum Status { PENDING, SHIPPED, CANCELLED }
}
