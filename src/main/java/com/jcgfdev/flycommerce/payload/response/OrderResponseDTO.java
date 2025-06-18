package com.jcgfdev.flycommerce.payload.response;

import com.jcgfdev.flycommerce.dto.OrderItemDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderResponseDTO {
    private String id;
    private UUID customerId;
    private String status;
    private List<OrderItemDTO> items;
    private Instant createdAt;
}
