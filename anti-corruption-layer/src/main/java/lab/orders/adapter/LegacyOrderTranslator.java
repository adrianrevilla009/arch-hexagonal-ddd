package lab.orders.adapter;

import java.math.BigDecimal;
import java.util.Map;
import lab.legacy.LegacyOrderRecord;
import lab.orders.domain.Order;
import lab.orders.domain.Order.Status;

/** The anti-corruption layer: the only place that understands both models. */
public final class LegacyOrderTranslator {
    private static final Map<String, Status> STATUS = Map.of("N", Status.PENDING, "S", Status.SHIPPED, "X", Status.CANCELLED);

    private LegacyOrderTranslator() {}

    public static Order toDomain(LegacyOrderRecord r) {
        Status status = STATUS.get(r.stat());
        if (status == null) throw new IllegalArgumentException("unknown legacy status: " + r.stat());
        long cents = new BigDecimal(r.amtStr().trim()).movePointRight(2).longValueExact();
        return new Order(r.ordNo().trim(), r.custCd().trim(), status, cents, r.ccy().trim().toUpperCase());
    }
}
