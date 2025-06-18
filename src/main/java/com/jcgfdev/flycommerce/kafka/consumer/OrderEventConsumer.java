package com.jcgfdev.flycommerce.kafka.consumer;

import com.jcgfdev.flycommerce.model.mongo.Order;
import com.jcgfdev.flycommerce.repository.mongo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "orderStatus", groupId = "flycommerce-orders")
    public void consumeOrderStatus(Order order) {
        log.info("Recibido update de status para orden {}", order.getId());
        orderRepository.save(order); // Puede tener lógica adicional para actualizar solo el status
    }
}