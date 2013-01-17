package pl.pragmatists.facadeservices;

import java.util.List;

public class OrderService {
    private OrderRepository orderRepository;
    private BillingSystem billingSystem;
    private LocationService locationService;
    private MessageService messageService;
    private InventoryManagement inventoryManagement;

    public OrderService(OrderRepository orderRepository, BillingSystem billingSystem, LocationService locationService, MessageService messageService, InventoryManagement inventoryManagement) {
        this.orderRepository = orderRepository;
        this.billingSystem = billingSystem;
        this.locationService = locationService;
        this.messageService = messageService;
        this.inventoryManagement = inventoryManagement;
    }

    public void receive(Order order) {
        orderRepository.save(order);

        List<Warehouse> warehouses = inventoryManagement.allWarehousesContaining(order.getItems());
        List<Warehouse> warehousesByProximity = locationService.sortByProximity(order.getDeliveryAddress(), warehouses);
        if (!warehousesByProximity.isEmpty())
            warehousesByProximity.get(0).pickAndShip(order.getItems());

        billingSystem.invoiceCreated(order.getTotal());
        messageService.sendReceipt(order);

    }
}
