package com.jcgfdev.flycommerce.service;

import com.jcgfdev.flycommerce.model.mongo.Order;
import com.jcgfdev.flycommerce.model.mongo.OrderItem;

import java.util.List;

public interface IOrderService {
    Order createOrder(Order order);
    Order updateOrderItems(String orderId, List<OrderItem> items);
    Order getOrderById(String orderId);
    List<Order> getOrdersByCustomer(String customerId);
    List<Order> searchOrders(String status, String customerId);
    void cancelOrder(String orderId);
}
