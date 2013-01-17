package pl.pragmatists.facadeservices

import spock.lang.Specification

class OrderServiceSpecification extends Specification {

    private static final int TOTAL = 5133
    private static final ArrayList<OrderItem> ITEMS = [new OrderItem()]
    def Location DELIVERY_ADDRESS = Mock(Location)
    def orderRepository = Mock(OrderRepository)
    def messageService = Mock(MessageService)
    def billingSystem = Mock(BillingSystem)
    def locationService = Mock(LocationService)
    def inventoryManagement = Mock(InventoryManagement)
    def orderService = new OrderService(orderRepository, billingSystem, locationService, messageService, inventoryManagement)

    def "saves the order on reception"() {
        setup:
        inventoryManagement.allWarehousesContaining(_) >> []
        locationService.sortByProximity(_, _) >> []

        when:
        orderService.receive(anOrder())

        then:
        1 * orderRepository.save(anOrder())
    }

    def "notify other systems on order reception"() {
        setup:
        inventoryManagement.allWarehousesContaining(_) >> []
        locationService.sortByProximity(_, _) >> []

        when:
        orderService.receive(anOrder())

        then:
        1 * messageService.sendReceipt(anOrder())
        1 * billingSystem.invoiceCreated(TOTAL)
    }

    def "select best warehouses to ship order"() {
        setup:
        def warehouseA = Mock(Warehouse)
        def warehouseB = Mock(Warehouse)
        def allWarehouses = [warehouseA, warehouseB]
        def sortedWarehouses = [warehouseB, warehouseA]
        inventoryManagement.allWarehousesContaining(ITEMS) >> allWarehouses
        locationService.sortByProximity(DELIVERY_ADDRESS, allWarehouses) >> sortedWarehouses

        when:
        orderService.receive(anOrder())

        then:
        1 * warehouseB.pickAndShip(ITEMS)
        0 * warehouseA.pickAndShip(ITEMS)
    }

    private Order anOrder() {
        new Order(id: "a test order", total: TOTAL, items: ITEMS, deliveryAddress: DELIVERY_ADDRESS)
    }
}
