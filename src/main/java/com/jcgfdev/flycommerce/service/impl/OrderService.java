package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.dto.OrderItemDTO;
import com.jcgfdev.flycommerce.exception.DataNotFoundException;
import com.jcgfdev.flycommerce.kafka.event.OrderToPaymentEvent;
import com.jcgfdev.flycommerce.kafka.producer.OrderEventProducer;
import com.jcgfdev.flycommerce.mapper.IOrderMapper;
import com.jcgfdev.flycommerce.model.mongo.Order;
import com.jcgfdev.flycommerce.payload.request.OrderRequestDTO;
import com.jcgfdev.flycommerce.payload.response.OrderResponseDTO;
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
    private final OrderEventProducer orderEventProducer;
    private final IOrderMapper orderMapper;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
        Order order = orderMapper.toEntity(orderRequest);
        order.setCreatedAt(Instant.now());
        order.setStatus("CREATED");
        Order savedOrder = orderRepository.save(order);

        OrderToPaymentEvent event = new OrderToPaymentEvent(
                savedOrder.getId(),
                calculateTotal(savedOrder)
        );
        orderEventProducer.sendOrderToPayment(event);
        log.info("Order event sent to OrderToPayment topic: {}", event);

        return orderMapper.toDto(savedOrder);
    }

    private Double calculateTotal(Order order) {
        return order.getItems().stream()
                .mapToDouble(i -> i.getQuantity() * i.getPrice())
                .sum();
    }

    @Override
    public OrderResponseDTO updateOrderItems(String orderId, List<OrderItemDTO> items) {
        Order order = getOrderByIdEntity(orderId);
        order.setItems(items.stream().map(orderMapper::toEntity).toList());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderResponseDTO getOrderById(String orderId) {
        return orderMapper.toDto(getOrderByIdEntity(orderId));
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderResponseDTO> searchOrders(String status, String customerId) {
        return orderRepository.findAll().stream()
                .filter(o -> (status == null || o.getStatus().equals(status)) &&
                        (customerId == null || o.getCustomerId().toString().equals(customerId)))
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = getOrderByIdEntity(orderId);
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }

    private Order getOrderByIdEntity(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
    }
}
