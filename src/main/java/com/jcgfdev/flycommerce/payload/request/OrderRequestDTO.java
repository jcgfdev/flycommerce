package com.jcgfdev.flycommerce.payload.request;

import com.jcgfdev.flycommerce.dto.OrderItemDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderRequestDTO {
    private UUID customerId;
    private List<OrderItemDTO> items;
}
