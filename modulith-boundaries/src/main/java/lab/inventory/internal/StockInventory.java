package lab.inventory.internal;

import java.util.HashMap;
import java.util.Map;
import lab.inventory.InventoryApi;

/** Module internals: hidden behind InventoryApi. */
public class StockInventory implements InventoryApi {
    private final Map<String, Integer> stock = new HashMap<>();

    public StockInventory(Map<String, Integer> initial) {
        stock.putAll(initial);
    }

    @Override
    public boolean reserve(String sku, int qty) {
        int have = stock.getOrDefault(sku, 0);
        if (qty <= 0 || have < qty) return false;
        stock.put(sku, have - qty);
        return true;
    }

    @Override
    public int available(String sku) {
        return stock.getOrDefault(sku, 0);
    }
}
