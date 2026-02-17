package com.example.order_service.service;

import com.example.order_service.command.CreateOrderCommand;
import com.example.order_service.domain.model.Order;
import com.example.order_service.domain.repository.OrderRepository;
import com.example.order_service.event.OrderEventPublisher;
import com.example.order_service.event.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Order createOrder(CreateOrderCommand command) {
        logger.info("Creating order for customer: {}", command.getCustomerName());
        
        Order order = new Order(
            command.getCustomerName(),
            command.getProduct(),
            command.getQuantity(),
            command.getPrice()
        );
        
        Order savedOrder = orderRepository.save(order);
        logger.info("Order saved with id: {}", savedOrder.getId());
        
        OrderPlacedEvent event = new OrderPlacedEvent(
            savedOrder.getId(),
            savedOrder.getCustomerName(),
            savedOrder.getProduct(),
            savedOrder.getQuantity(),
            savedOrder.getPrice(),
            savedOrder.getCreatedAt()
        );
        
        eventPublisher.publishOrderPlaced(event);
        logger.info("OrderPlaced event published for order: {}", savedOrder.getId());
        
        return savedOrder;
    }
}
