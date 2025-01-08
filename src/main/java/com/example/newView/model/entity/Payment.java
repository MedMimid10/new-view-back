package com.example.newView.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String transactionId;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime paymentDate;

    // Card details (in real application, these should be handled by a payment processor)
    private String lastFourDigits; // Store only last 4 digits for reference
    private String cardType;
}
