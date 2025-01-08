package com.example.newView.model.dto;

import com.example.newView.model.entity.OrderStatus;
import com.example.newView.model.entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponseDTO {
    private Long id;
    private String username;
    private List<OrderItemDTO> items;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private LocalDateTime confirmedDate;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
    private String trackingNumber;
}
