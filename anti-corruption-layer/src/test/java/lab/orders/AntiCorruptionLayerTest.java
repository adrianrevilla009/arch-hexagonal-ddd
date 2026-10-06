package lab.orders;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import lab.legacy.LegacyOrderRecord;
import lab.orders.adapter.LegacyOrderTranslator;
import lab.orders.adapter.LegacyOrdersAdapter;
import lab.orders.application.LegacyOrders;
import lab.orders.domain.Order;
import org.junit.jupiter.api.Test;

class AntiCorruptionLayerTest {
    private final LegacyOrderRecord shipped = new LegacyOrderRecord(" A-1 ", "C9", "S", "12.50", "eur");

    @Test
    void translatesLegacyRecordToDomainModel() {
        Order o = LegacyOrderTranslator.toDomain(shipped);
        assertEquals(new Order("A-1", "C9", Order.Status.SHIPPED, 1250, "EUR"), o);
    }

    @Test
    void unknownStatusIsRejectedAtTheBoundary() {
        var bad = new LegacyOrderRecord("A-2", "C9", "?", "1.00", "EUR");
        assertThrows(IllegalArgumentException.class, () -> LegacyOrderTranslator.toDomain(bad));
    }

    @Test
    void sub_centAmountsAreRejectedNotRounded() {
        var bad = new LegacyOrderRecord("A-3", "C9", "N", "1.005", "EUR");
        assertThrows(ArithmeticException.class, () -> LegacyOrderTranslator.toDomain(bad));
    }

    @Test
    void portReturnsOnlyDomainTypes() {
        LegacyOrders port = new LegacyOrdersAdapter(Map.of("A-1", shipped));
        assertTrue(port.findById("A-1").isPresent());
        assertTrue(port.findById("missing").isEmpty());
    }
}
