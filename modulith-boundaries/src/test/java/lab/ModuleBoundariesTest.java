package lab;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.junit.jupiter.api.Assertions.*;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import java.util.Map;
import lab.inventory.internal.StockInventory;
import lab.orders.OrdersApi;
import org.junit.jupiter.api.Test;

class ModuleBoundariesTest {
    /** A module's internal package may only be used from inside the same module. */
    static ArchRule internalsAreHidden(String module) {
        return noClasses().that().resideOutsideOfPackage("lab." + module + "..")
                .should().dependOnClassesThat().resideInAPackage("lab." + module + ".internal..");
    }

    @Test
    void realModulesRespectBoundaries() {
        JavaClasses main = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("lab.orders", "lab.inventory");
        internalsAreHidden("inventory").check(main);
        internalsAreHidden("orders").check(main);
    }

    @Test
    void rulesRejectViolatingFixture() {
        JavaClasses bad = new ClassFileImporter().importPackages("lab.fixture");
        assertThrows(AssertionError.class, () -> internalsAreHidden("inventory").check(bad));
    }

    @Test
    void modulesCollaborateThroughApiOnly() {
        OrdersApi orders = new OrdersApi(new StockInventory(Map.of("sku-1", 2)));
        assertTrue(orders.placeOrder("sku-1", 2));
        assertFalse(orders.placeOrder("sku-1", 1));
    }
}
