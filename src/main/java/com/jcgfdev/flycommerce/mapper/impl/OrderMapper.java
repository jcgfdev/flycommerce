package com.jcgfdev.flycommerce.mapper.impl;

import com.jcgfdev.flycommerce.dto.OrderItemDTO;
import com.jcgfdev.flycommerce.mapper.IOrderMapper;
import com.jcgfdev.flycommerce.model.mongo.Order;
import com.jcgfdev.flycommerce.model.mongo.OrderItem;
import com.jcgfdev.flycommerce.payload.request.OrderRequestDTO;
import com.jcgfdev.flycommerce.payload.response.OrderResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper implements IOrderMapper {
    @Override
    public Order toEntity(OrderRequestDTO dto) {
        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setItems(dto.getItems().stream().map(this::toEntity).toList());
        return order;
    }

    @Override
    public OrderItem toEntity(OrderItemDTO dto) {
        return new OrderItem(dto.getProductId(), dto.getName(), dto.getQuantity(), dto.getPrice());
    }

    @Override
    public OrderResponseDTO toDto(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setItems(order.getItems().stream().map(this::toDto).toList());
        return dto;
    }

    @Override
    public OrderItemDTO toDto(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(item.getProductId());
        dto.setName(item.getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }
}
