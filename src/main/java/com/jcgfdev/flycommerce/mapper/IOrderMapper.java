package com.jcgfdev.flycommerce.mapper;

import com.jcgfdev.flycommerce.dto.OrderItemDTO;
import com.jcgfdev.flycommerce.model.mongo.Order;
import com.jcgfdev.flycommerce.model.mongo.OrderItem;
import com.jcgfdev.flycommerce.payload.request.OrderRequestDTO;
import com.jcgfdev.flycommerce.payload.response.OrderResponseDTO;

public interface IOrderMapper {
    Order toEntity(OrderRequestDTO dto);
    OrderItem toEntity(OrderItemDTO dto);
    OrderResponseDTO toDto(Order order);
    OrderItemDTO toDto(OrderItem item);
}
