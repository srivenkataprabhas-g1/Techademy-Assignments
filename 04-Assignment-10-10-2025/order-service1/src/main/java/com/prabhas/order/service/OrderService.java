package com.prabhas.order.service;


import com.prabhas.order.dto.OrderRequest;
import com.prabhas.order.dto.OrderResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<OrderResponse> createOrder(OrderRequest orderRequest);
    OrderResponse getOrderById(Long orderId);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrdersByCustomer(String customerName);
}
