package com.example.projection_service.service;

import com.example.projection_service.domain.model.Order;
import com.example.projection_service.domain.repository.OrderRepository;
import com.example.projection_service.event.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handleOrderPlaced(OrderPlacedEvent event) {
        logger.info("Handling OrderPlaced event for order: {}", event.getOrderId());
        
        Order order = new Order(
            event.getOrderId(),
            event.getCustomerName(),
            event.getProduct(),
            event.getQuantity(),
            event.getPrice(),
            event.getCreatedAt()
        );
        
        orderRepository.save(order);
        logger.info("Order saved to read database: {}", event.getOrderId());
        
        eventPublisher.publishEvent(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }
}
