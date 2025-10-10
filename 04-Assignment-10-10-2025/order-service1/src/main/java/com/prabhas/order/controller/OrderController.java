package com.prabhas.order.controller;

import com.prabhas.order.dto.OrderRequest;
import com.prabhas.order.dto.OrderResponse;
import com.prabhas.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public CompletableFuture<ResponseEntity<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("Received order creation request: {}", orderRequest);
        
        return orderService.createOrder(orderRequest)
                .thenApply(orderResponse -> {
                    log.info("Order created successfully: {}", orderResponse.getOrderId());
                    return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
                })
                .exceptionally(throwable -> {
                    log.error("Error creating order: {}", throwable.getMessage());
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                });
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        log.info("Fetching order with ID: {}", orderId);
        
        try {
            OrderResponse orderResponse = orderService.getOrderById(orderId);
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error fetching order: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        log.info("Fetching all orders");
        
        List<OrderResponse> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable String customerName) {
        log.info("Fetching orders for customer: {}", customerName);
        
        List<OrderResponse> orders = orderService.getOrdersByCustomer(customerName);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}