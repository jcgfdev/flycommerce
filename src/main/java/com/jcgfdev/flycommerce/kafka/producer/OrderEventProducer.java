package com.jcgfdev.flycommerce.kafka.producer;

import com.jcgfdev.flycommerce.kafka.event.OrderToPaymentEvent;
import com.jcgfdev.flycommerce.model.mongo.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderToPaymentEvent> kafkaTemplate;

    public void sendOrderToPayment(OrderToPaymentEvent event) {
        log.info("Enviando evento de orden a payment: {}", event.getOrderId());
        kafkaTemplate.send("OrderToPayment", event);
    }
}
