package com.example.order_service.controller;

import com.example.order_service.command.CreateOrderCommand;
import com.example.order_service.domain.model.Order;
import com.example.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/commands/orders")
public class OrderCommandController {

    private final OrderService orderService;

    public OrderCommandController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderCommand command) {
        Order order = orderService.createOrder(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
