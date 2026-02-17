package com.example.order_service.controller;

import com.example.order_service.command.CreateOrderCommand;
import com.example.order_service.domain.model.Order;
import com.example.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/commands/orders")
public class OrderCommandController {

    private final OrderService orderService;

    public OrderCommandController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@Valid @RequestBody CreateOrderCommand command) {
        Order order = orderService.createOrder(command);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", order.getId());
        response.put("customerName", order.getCustomerName());
        response.put("product", order.getProduct());
        response.put("quantity", order.getQuantity());
        response.put("price", order.getPrice());
        response.put("createdAt", order.getCreatedAt());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
