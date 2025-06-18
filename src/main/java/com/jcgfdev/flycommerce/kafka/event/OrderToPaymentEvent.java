package com.jcgfdev.flycommerce.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderToPaymentEvent {
    private String orderId;
    private Double totalAmount;
}
