package com.example.demo.controller;

import com.example.demo.readmodel.ShiggyReadModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final ShiggyReadModel readModel;
    
    public WebSocketController(SimpMessagingTemplate messagingTemplate, ShiggyReadModel readModel) {
        this.messagingTemplate = messagingTemplate;
        this.readModel = readModel;
    }
    
    public void broadcastStatus() {
        messagingTemplate.convertAndSend("/topic/status", readModel);
    }
    
    @MessageMapping("/requestStatus")
    @SendTo("/topic/status")
    public ShiggyReadModel getStatus() {
        return readModel;
    }
}
