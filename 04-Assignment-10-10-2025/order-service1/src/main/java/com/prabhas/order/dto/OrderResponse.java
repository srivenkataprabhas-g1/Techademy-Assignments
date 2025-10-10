package com.prabhas.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private String customerName;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private String status;
}
