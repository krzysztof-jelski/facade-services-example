package pl.pragmatists.facadeservices

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
class Order {
    String id
    int total
    List<OrderItem> items
    Location deliveryAddress
}
