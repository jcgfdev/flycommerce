package com.jcgfdev.flycommerce.controller;

import com.jcgfdev.flycommerce.dto.OrderItemDTO;
import com.jcgfdev.flycommerce.payload.request.OrderRequestDTO;
import com.jcgfdev.flycommerce.payload.response.OrderResponseDTO;
import com.jcgfdev.flycommerce.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        OrderResponseDTO createdOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{id}/items")
    public ResponseEntity<OrderResponseDTO> updateItems(@PathVariable String id, @RequestBody List<OrderItemDTO> items) {
        return ResponseEntity.ok(orderService.updateOrderItems(id, items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> getByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderResponseDTO>> search(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String customerId) {
        return ResponseEntity.ok(orderService.searchOrders(status, customerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable String id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
