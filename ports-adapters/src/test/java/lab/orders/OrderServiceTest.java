package lab.orders;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lab.orders.application.OrderRepository;
import lab.orders.application.OrderService;
import lab.orders.domain.Order;
import org.junit.jupiter.api.Test;

class OrderServiceTest {
    /** A test double implementing the port: the use case runs with no real adapter. */
    static class FakeRepo implements OrderRepository {
        final List<Order> saved = new ArrayList<>();

        public void save(Order o) { saved.add(o); }

        public Optional<Order> findById(UUID id) {
            return saved.stream().filter(o -> o.id().equals(id)).findFirst();
        }
    }

    @Test
    void placeStoresOrderThroughPort() {
        FakeRepo repo = new FakeRepo();
        UUID id = new OrderService(repo).place("bob", 500);
        assertEquals("bob", repo.findById(id).orElseThrow().customer());
    }

    @Test
    void negativeTotalRejected() {
        assertThrows(IllegalArgumentException.class, () -> new OrderService(new FakeRepo()).place("bob", -1));
    }
}
