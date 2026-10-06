package lab.orders.application;

import java.util.UUID;

/** Driving (inbound) port: what the outside world may ask of the application. */
public interface PlaceOrder {
    UUID place(String customer, long totalCents);
}
