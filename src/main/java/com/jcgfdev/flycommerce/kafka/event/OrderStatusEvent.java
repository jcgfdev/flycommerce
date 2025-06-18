package com.jcgfdev.flycommerce.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusEvent {
    private String orderId;
    private String status;
    private String message;
}
