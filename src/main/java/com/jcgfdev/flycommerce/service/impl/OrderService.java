package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.exception.DataNotFoundException;
import com.jcgfdev.flycommerce.model.mongo.Order;
import com.jcgfdev.flycommerce.model.mongo.OrderItem;
import com.jcgfdev.flycommerce.repository.mongo.OrderRepository;
import com.jcgfdev.flycommerce.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        order.setCreatedAt(Instant.now());
        order.setStatus("CREATED");
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrderItems(String orderId, List<OrderItem> items) {
        Order order = getOrderById(orderId);
        order.setItems(items);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
    }

    @Override
    public List<Order> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Order> searchOrders(String status, String customerId) {
        return orderRepository.findAll().stream()
                .filter(o -> (status == null || o.getStatus().equals(status)) &&
                        (customerId == null || o.getCustomerId().toString().equals(customerId)))
                .toList();
    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = getOrderById(orderId);
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }
}
