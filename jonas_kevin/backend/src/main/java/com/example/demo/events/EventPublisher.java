package com.example.demo.events;

import com.example.demo.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
    
    private final RabbitTemplate rabbitTemplate;
    
    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void publish(Object event) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY,
            event
        );
        System.out.println("Event published: " + event.getClass().getSimpleName());
    }
}
