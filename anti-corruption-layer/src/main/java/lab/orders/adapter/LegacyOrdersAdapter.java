package lab.orders.adapter;

import java.util.Map;
import java.util.Optional;
import lab.legacy.LegacyOrderRecord;
import lab.orders.application.LegacyOrders;
import lab.orders.domain.Order;

/** Adapter over a stand-in legacy client; every record is translated before leaving. */
public class LegacyOrdersAdapter implements LegacyOrders {
    private final Map<String, LegacyOrderRecord> legacyClient;

    public LegacyOrdersAdapter(Map<String, LegacyOrderRecord> legacyClient) {
        this.legacyClient = legacyClient;
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(legacyClient.get(id)).map(LegacyOrderTranslator::toDomain);
    }
}
