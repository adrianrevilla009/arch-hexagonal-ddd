package lab.orders;

import lab.inventory.InventoryApi;

/** Orders module entry point; talks to inventory only through its public API. */
public class OrdersApi {
    private final InventoryApi inventory;

    public OrdersApi(InventoryApi inventory) {
        this.inventory = inventory;
    }

    public boolean placeOrder(String sku, int qty) {
        return inventory.reserve(sku, qty);
    }
}
