package com.jcgfdev.flycommerce.service;

import com.jcgfdev.flycommerce.dto.OrderItemDTO;
import com.jcgfdev.flycommerce.payload.request.OrderRequestDTO;
import com.jcgfdev.flycommerce.payload.response.OrderResponseDTO;

import java.util.List;

public interface IOrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequest);
    OrderResponseDTO updateOrderItems(String orderId, List<OrderItemDTO> items);
    OrderResponseDTO getOrderById(String orderId);
    List<OrderResponseDTO> getOrdersByCustomer(String customerId);
    List<OrderResponseDTO> searchOrders(String status, String customerId);
    void cancelOrder(String orderId);
}
