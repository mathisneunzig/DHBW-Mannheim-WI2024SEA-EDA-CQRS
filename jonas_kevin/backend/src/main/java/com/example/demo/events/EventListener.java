package com.example.demo.events;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.controller.WebSocketController;
import com.example.demo.events.domain.*;
import com.example.demo.readmodel.ShiggyReadModelUpdater;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {
    
    private final ShiggyReadModelUpdater readModelUpdater;
    private final ObjectMapper objectMapper;
    private final WebSocketController webSocketController;
    
    public EventListener(ShiggyReadModelUpdater readModelUpdater, ObjectMapper objectMapper, WebSocketController webSocketController) {
        this.readModelUpdater = readModelUpdater;
        this.objectMapper = objectMapper;
        this.webSocketController = webSocketController;
    }
    
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleEvent(Message message) {
        try {
            String typeId = (String) message.getMessageProperties().getHeaders().get("__TypeId__");
            System.out.println("Received message with type: " + typeId);
            
            if (typeId == null) {
                System.out.println("No type information in message headers");
                return;
            }
            
            byte[] body = message.getBody();
            
            if (typeId.contains("ShiggyFedEvent")) {
                ShiggyFedEvent event = objectMapper.readValue(body, ShiggyFedEvent.class);
                System.out.println("Processing ShiggyFedEvent: " + event.getEventId());
                readModelUpdater.updateFromFedEvent(event);
                webSocketController.broadcastStatus();
            } else if (typeId.contains("ShiggyPlayedEvent")) {
                ShiggyPlayedEvent event = objectMapper.readValue(body, ShiggyPlayedEvent.class);
                System.out.println("Processing ShiggyPlayedEvent: " + event.getEventId());
                readModelUpdater.updateFromPlayedEvent(event);
                webSocketController.broadcastStatus();
            } else if (typeId.contains("ShiggySleptEvent")) {
                ShiggySleptEvent event = objectMapper.readValue(body, ShiggySleptEvent.class);
                System.out.println("Processing ShiggySleptEvent: " + event.getEventId());
                readModelUpdater.updateFromSleptEvent(event);
                webSocketController.broadcastStatus();
            } else {
                System.out.println("Unknown event type: " + typeId);
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
