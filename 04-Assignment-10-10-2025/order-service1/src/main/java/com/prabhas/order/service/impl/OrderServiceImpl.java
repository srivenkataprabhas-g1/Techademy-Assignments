package com.prabhas.order.service.impl;

import com.prabhas.order.dto.OrderRequest;
import com.prabhas.order.dto.OrderResponse;
import com.prabhas.order.dto.ProductResponse;
import com.prabhas.order.model.Order;
import com.prabhas.order.repository.OrderRepository;
import com.prabhas.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient productServiceWebClient;

    @Override
    @Async
    public CompletableFuture<OrderResponse> createOrder(OrderRequest orderRequest) {
        log.info("Creating order for customer: {} and product ID: {}", 
                orderRequest.getCustomerName(), orderRequest.getProductId());

        return getProductDetailsAsync(orderRequest.getProductId())
                .thenCompose(productResponse -> {
                    if (productResponse == null) {
                        log.error("Product not found with ID: {}", orderRequest.getProductId());
                        throw new RuntimeException("Product not found");
                    }

                    if (productResponse.getStockQuantity() < orderRequest.getQuantity()) {
                        log.error("Insufficient stock for product ID: {}. Available: {}, Requested: {}", 
                                orderRequest.getProductId(), productResponse.getStockQuantity(), orderRequest.getQuantity());
                        throw new RuntimeException("Insufficient stock");
                    }

                    // Create order
                    Order order = Order.builder()
                            .productId(orderRequest.getProductId())
                            .quantity(orderRequest.getQuantity())
                            .customerName(orderRequest.getCustomerName())
                            .totalAmount(productResponse.getPrice() * orderRequest.getQuantity())
                            .orderDate(LocalDateTime.now())
                            .status("CREATED")
                            .build();

                    Order savedOrder = orderRepository.save(order);
                    log.info("Order created successfully with ID: {}", savedOrder.getId());

                    return CompletableFuture.completedFuture(buildOrderResponse(savedOrder, productResponse));
                });
    }

    private CompletableFuture<ProductResponse> getProductDetailsAsync(Long productId) {
        log.info("Fetching product details for ID: {}", productId);

        Mono<ProductResponse> productMono = productServiceWebClient
                .get()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .timeout(Duration.ofSeconds(5))
                .doOnError(error -> log.error("Error fetching product details: {}", error.getMessage()))
                .onErrorReturn(ProductResponse.builder().build());

        return productMono.toFuture();
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        log.info("Fetching order with ID: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Get product details synchronously for this method
        ProductResponse productResponse = productServiceWebClient
                .get()
                .uri("/api/products/{id}", order.getProductId())
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .block();

        return buildOrderResponse(order, productResponse);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders");
        
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> {
                    ProductResponse productResponse = productServiceWebClient
                            .get()
                            .uri("/api/products/{id}", order.getProductId())
                            .retrieve()
                            .bodyToMono(ProductResponse.class)
                            .block();
                    return buildOrderResponse(order, productResponse);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByCustomer(String customerName) {
        log.info("Fetching orders for customer: {}", customerName);
        
        List<Order> orders = orderRepository.findByCustomerName(customerName);
        return orders.stream()
                .map(order -> {
                    ProductResponse productResponse = productServiceWebClient
                            .get()
                            .uri("/api/products/{id}", order.getProductId())
                            .retrieve()
                            .bodyToMono(ProductResponse.class)
                            .block();
                    return buildOrderResponse(order, productResponse);
                })
                .collect(Collectors.toList());
    }

    private OrderResponse buildOrderResponse(Order order, ProductResponse productResponse) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .productName(productResponse != null ? productResponse.getName() : "Unknown")
                .productPrice(productResponse != null ? productResponse.getPrice() : 0.0)
                .quantity(order.getQuantity())
                .customerName(order.getCustomerName())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .build();
    }
}
