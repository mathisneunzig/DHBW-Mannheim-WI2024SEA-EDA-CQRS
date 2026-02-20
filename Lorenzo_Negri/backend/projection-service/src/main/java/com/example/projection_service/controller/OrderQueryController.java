package com.example.projection_service.controller;

import com.example.projection_service.domain.model.Order;
import com.example.projection_service.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/queries")
public class OrderQueryController {

    private static final Logger logger = LoggerFactory.getLogger(OrderQueryController.class);

    private final OrderService orderService;
    private final ObjectMapper objectMapper;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public OrderQueryController(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/stream/orders", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamOrders() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(error -> {
            logger.error("SSE error: {}", error.getMessage());
            emitters.remove(emitter);
        });

        return emitter;
    }

    @EventListener
    public void handleOrderCreated(Order order) {
        broadcastOrderEvent(order);
    }

    private void broadcastOrderEvent(Order order) {
        try {
            String json = objectMapper.writeValueAsString(order);
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .name("order-event")
                    .data(json);
            
            emitters.forEach(emitter -> {
                try {
                    emitter.send(event);
                } catch (IOException e) {
                    logger.error("Error sending to emitter: {}", e.getMessage());
                    emitters.remove(emitter);
                }
            });
        } catch (IOException e) {
            logger.error("Error serializing order: {}", e.getMessage());
        }
    }
}
