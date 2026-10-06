package lab.fixture.orders;

/** Intentionally violates the boundary: reaches into another module's internal package. */
public class Sneaky {
    public lab.inventory.internal.StockInventory direct;
}
