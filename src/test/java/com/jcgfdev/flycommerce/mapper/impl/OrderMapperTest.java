package com.jcgfdev.flycommerce.mapper.impl;

import com.jcgfdev.flycommerce.dto.OrderItemDTO;
import com.jcgfdev.flycommerce.model.mongo.Order;
import com.jcgfdev.flycommerce.model.mongo.OrderItem;
import com.jcgfdev.flycommerce.payload.request.OrderRequestDTO;
import com.jcgfdev.flycommerce.payload.response.OrderResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private final OrderMapper mapper = new OrderMapper();

    @Test
    void toEntity_fromOrderRequestDTO_shouldMapCorrectly() {
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId("p1");
        itemDTO.setName("Product 1");
        itemDTO.setQuantity(2);
        itemDTO.setPrice(10.0);
        UUID customerId = UUID.randomUUID();

        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setCustomerId(customerId);
        requestDTO.setItems(List.of(itemDTO));

        Order order = mapper.toEntity(requestDTO);

        assertNotNull(order);
        assertEquals(customerId, order.getCustomerId());
        assertEquals(1, order.getItems().size());

        OrderItem item = order.getItems().get(0);
        assertEquals("p1", item.getProductId());
        assertEquals("Product 1", item.getName());
        assertEquals(2, item.getQuantity());
        assertEquals(10.0, item.getPrice());
    }

    @Test
    void toEntity_fromOrderItemDTO_shouldMapCorrectly() {
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId("p2");
        itemDTO.setName("Product 2");
        itemDTO.setQuantity(3);
        itemDTO.setPrice(15.5);
        OrderItem item = mapper.toEntity(itemDTO);

        assertNotNull(item);
        assertEquals("p2", item.getProductId());
        assertEquals("Product 2", item.getName());
        assertEquals(3, item.getQuantity());
        assertEquals(15.5, item.getPrice());
    }

    @Test
    void toDto_fromOrder_shouldMapCorrectly() {
        OrderItem item = new OrderItem("p3", "Product 3", 1, 5.0);
        UUID customerId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        Order order = new Order();
        order.setId("order123");
        order.setCustomerId(customerId);
        order.setStatus("CREATED");
        order.setCreatedAt(createdAt);
        order.setItems(List.of(item));

        OrderResponseDTO dto = mapper.toDto(order);

        assertNotNull(dto);
        assertEquals("order123", dto.getId());
        assertEquals(customerId, dto.getCustomerId());
        assertEquals("CREATED", dto.getStatus());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(1, dto.getItems().size());

        OrderItemDTO itemDTO = dto.getItems().get(0);
        assertEquals("p3", itemDTO.getProductId());
        assertEquals("Product 3", itemDTO.getName());
        assertEquals(1, itemDTO.getQuantity());
        assertEquals(5.0, itemDTO.getPrice());
    }

    @Test
    void toDto_fromOrderItem_shouldMapCorrectly() {
        OrderItem item = new OrderItem("p4", "Product 4", 4, 20.0);

        OrderItemDTO dto = mapper.toDto(item);

        assertNotNull(dto);
        assertEquals("p4", dto.getProductId());
        assertEquals("Product 4", dto.getName());
        assertEquals(4, dto.getQuantity());
        assertEquals(20.0, dto.getPrice());
    }
}