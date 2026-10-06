package lab.inventory;

/** Public API of the inventory module: the only type other modules may use. */
public interface InventoryApi {
    boolean reserve(String sku, int qty);

    int available(String sku);
}
