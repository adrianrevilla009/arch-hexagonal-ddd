package lab.orders.domain;

/** Pure domain: only java.lang / java.util / java.time are allowed here. */
public record Order(String id, long totalCents) {}
