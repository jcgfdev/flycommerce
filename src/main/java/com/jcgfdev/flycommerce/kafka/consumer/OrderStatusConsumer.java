package com.jcgfdev.flycommerce.kafka.consumer;

import com.jcgfdev.flycommerce.kafka.event.OrderStatusEvent;
import com.jcgfdev.flycommerce.model.mongo.Order;
import com.jcgfdev.flycommerce.repository.mongo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderStatusConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "orderStatus", groupId = "order-service-group")
    public void listen(OrderStatusEvent event) {
        log.info("Received order status event: {}", event);

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + event.getOrderId()));

        order.setStatus(event.getStatus());
        orderRepository.save(order);

        log.info("Order {} updated with status {}", event.getOrderId(), event.getStatus());
    }
}
