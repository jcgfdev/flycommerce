package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.dto.OrderItemDTO;
import com.jcgfdev.flycommerce.exception.DataNotFoundException;
import com.jcgfdev.flycommerce.kafka.producer.OrderEventProducer;
import com.jcgfdev.flycommerce.mapper.IOrderMapper;
import com.jcgfdev.flycommerce.model.mongo.Order;
import com.jcgfdev.flycommerce.model.mongo.OrderItem;
import com.jcgfdev.flycommerce.payload.request.OrderRequestDTO;
import com.jcgfdev.flycommerce.payload.response.OrderResponseDTO;
import com.jcgfdev.flycommerce.repository.mongo.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventProducer orderEventProducer;

    @Mock
    private IOrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_savesOrderAndSendsEventProducer() {
        OrderRequestDTO requestDTO = new OrderRequestDTO(); // ajusta según campos
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setProductId("001");
        item.setName("item1");
        item.setQuantity(2);
        item.setPrice(10.0);
        order.setItems(List.of(item));

        Order savedOrder = new Order();
        savedOrder.setId("order123");
        savedOrder.setItems(order.getItems());
        savedOrder.setStatus("CREATED");
        savedOrder.setCreatedAt(Instant.now());

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId("order123");
        responseDTO.setStatus("CREATED");

        when(orderMapper.toEntity(requestDTO)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toDto(savedOrder)).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.createOrder(requestDTO);

        assertNotNull(result);
        assertEquals("CREATED", result.getStatus());

        verify(orderRepository).save(any(Order.class));
        verify(orderEventProducer).sendOrderToPayment(argThat(event ->
                event.getOrderId().equals("order123") &&
                        event.getTotalAmount() == 20.0
        ));
    }

    @Test
    void getOrderById_whenExists_returnsOrderResponseDTO() {
        Order order = new Order();
        order.setId("order123");

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId("order123");

        when(orderRepository.findById("order123")).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.getOrderById("order123");

        assertNotNull(result);
        assertEquals("order123", result.getId());
    }

    @Test
    void getOrderById_whenNotFound_throwsException() {
        when(orderRepository.findById("order123")).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> orderService.getOrderById("order123"));
    }

    @Test
    void updateOrderItems_updatesItemsAndSaves() {
        Order order = new Order();
        order.setId("order123");
        OrderItemDTO item = new OrderItemDTO();
        item.setProductId("001");
        item.setName("item1");
        item.setQuantity(2);
        item.setPrice(5.0);
        List<OrderItemDTO> itemDTOs = List.of(item);
        List<OrderItem> items = List.of(new OrderItem("001", "item2", 1, 5.0));

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId("order123");

        when(orderRepository.findById("order123")).thenReturn(Optional.of(order));
        when(orderMapper.toEntity(any(OrderItemDTO.class))).thenReturn(items.get(0));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.updateOrderItems("order123", itemDTOs);

        assertNotNull(result);
        assertEquals("order123", result.getId());
        verify(orderRepository).save(order);
    }

    @Test
    void getOrdersByCustomer_returnsOrderResponseDTOList() {
        Order order = new Order();
        order.setId("order123");
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId("order123");

        when(orderRepository.findByCustomerId("customer123")).thenReturn(List.of(order));
        when(orderMapper.toDto(order)).thenReturn(responseDTO);

        List<OrderResponseDTO> result = orderService.getOrdersByCustomer("customer123");

        assertEquals(1, result.size());
        assertEquals("order123", result.get(0).getId());
    }

    @Test
    void searchOrders_filtersAndMapsCorrectly() {
        UUID customerId = UUID.randomUUID();

        Order o1 = new Order();
        o1.setStatus("CREATED");
        o1.setCustomerId(customerId);

        Order o2 = new Order();
        o2.setStatus("CANCELLED");
        o2.setCustomerId(UUID.randomUUID());

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId("order123");

        when(orderRepository.findAll()).thenReturn(List.of(o1, o2));
        when(orderMapper.toDto(o1)).thenReturn(responseDTO);

        List<OrderResponseDTO> result = orderService.searchOrders("CREATED", customerId.toString());

        assertEquals(1, result.size());
        assertEquals("order123", result.get(0).getId());
    }

    @Test
    void cancelOrder_setsStatusCancelledAndSaves() {
        Order order = new Order();
        order.setId("order123");

        when(orderRepository.findById("order123")).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.cancelOrder("order123");

        assertEquals("CANCELLED", order.getStatus());
        verify(orderRepository).save(order);
    }
}
