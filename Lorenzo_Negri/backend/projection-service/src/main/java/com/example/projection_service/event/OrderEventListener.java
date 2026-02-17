package com.example.projection_service.event;

import com.example.projection_service.config.RabbitMQConfig;
import com.example.projection_service.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);

    private final OrderService orderService;

    public OrderEventListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE, messageConverter = "jsonMessageConverter")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        logger.info("Received OrderPlaced event: {}", event.getEventType());
        
        try {
            orderService.handleOrderPlaced(event);
        } catch (Exception e) {
            logger.error("Error processing OrderPlaced event: {}", e.getMessage(), e);
            throw e;
        }
    }
}
