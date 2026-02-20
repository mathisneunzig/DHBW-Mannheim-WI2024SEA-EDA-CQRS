package com.example.order_service.service;

import com.example.common.OrderPlacedEvent;
import com.example.order_service.command.CreateOrderCommand;
import com.example.order_service.domain.model.Order;
import com.example.order_service.domain.repository.OrderRepository;
import com.example.order_service.event.OrderEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisher eventPublisher;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, eventPublisher);
    }

    @Test
    void createOrder_shouldSaveOrderAndPublishEvent() {
        CreateOrderCommand command = new CreateOrderCommand(
            "John Doe",
            "Laptop",
            BigDecimal.valueOf(2),
            BigDecimal.valueOf(999.99)
        );

        Order savedOrder = new Order(
            "John Doe",
            "Laptop",
            BigDecimal.valueOf(2),
            BigDecimal.valueOf(999.99)
        );
        savedOrder.setId(UUID.randomUUID().toString());
        savedOrder.setCreatedAt(LocalDateTime.now());

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.createOrder(command);

        assertNotNull(result);
        assertEquals("John Doe", result.getCustomerName());
        assertEquals("Laptop", result.getProduct());
        
        verify(orderRepository, times(1)).save(any(Order.class));
        
        ArgumentCaptor<OrderPlacedEvent> eventCaptor = ArgumentCaptor.forClass(OrderPlacedEvent.class);
        verify(eventPublisher, times(1)).publishOrderPlaced(eventCaptor.capture());
        
        OrderPlacedEvent capturedEvent = eventCaptor.getValue();
        assertEquals("John Doe", capturedEvent.getCustomerName());
        assertEquals("Laptop", capturedEvent.getProduct());
    }

    @Test
    void createOrder_shouldGenerateIdIfNotPresent() {
        CreateOrderCommand command = new CreateOrderCommand(
            "Jane Smith",
            "Phone",
            BigDecimal.valueOf(1),
            BigDecimal.valueOf(599.99)
        );

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(UUID.randomUUID().toString());
            order.setCreatedAt(LocalDateTime.now());
            return order;
        });

        Order result = orderService.createOrder(command);

        assertNotNull(result.getId());
        assertFalse(result.getId().isEmpty());
    }

    @Test
    void createOrder_shouldSetCreatedAtTimestamp() {
        CreateOrderCommand command = new CreateOrderCommand(
            "Bob Wilson",
            "Tablet",
            BigDecimal.valueOf(3),
            BigDecimal.valueOf(299.99)
        );

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(UUID.randomUUID().toString());
            order.setCreatedAt(LocalDateTime.now());
            return order;
        });

        Order result = orderService.createOrder(command);

        assertNotNull(result.getCreatedAt());
    }

    @Test
    void createOrder_shouldPublishCorrectEventData() {
        CreateOrderCommand command = new CreateOrderCommand(
            "Alice Brown",
            "Monitor",
            BigDecimal.valueOf(2),
            BigDecimal.valueOf(349.99)
        );

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId("test-order-id");
            order.setCreatedAt(LocalDateTime.of(2026, 1, 15, 10, 30));
            return order;
        });

        orderService.createOrder(command);

        ArgumentCaptor<OrderPlacedEvent> eventCaptor = ArgumentCaptor.forClass(OrderPlacedEvent.class);
        verify(eventPublisher).publishOrderPlaced(eventCaptor.capture());

        OrderPlacedEvent event = eventCaptor.getValue();
        assertEquals("test-order-id", event.getOrderId());
        assertEquals("Alice Brown", event.getCustomerName());
        assertEquals("Monitor", event.getProduct());
        assertEquals(BigDecimal.valueOf(2), event.getQuantity());
        assertEquals(BigDecimal.valueOf(349.99), event.getPrice());
    }
}
