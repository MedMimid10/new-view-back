package com.example.newView.model.dto;

import com.example.newView.model.entity.OrderStatus;
import com.example.newView.model.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long paymentId;
    private Long orderId;
    private String transactionId;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private String cardLastFourDigits;
    private String cardType;

    // Optional: Add these fields if you want to include order information
    private OrderStatus orderStatus;
    private String customerName;
}