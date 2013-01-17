package pl.pragmatists.facadeservices;

import java.util.List;

public interface InventoryManagement {
    List<Warehouse> allWarehousesContaining(List<OrderItem> items);
}
