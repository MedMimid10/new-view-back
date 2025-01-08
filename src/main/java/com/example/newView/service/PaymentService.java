package com.example.newView.service;

import com.example.newView.exception.InvalidOperationException;
import com.example.newView.exception.ResourceNotFoundException;
import com.example.newView.model.dto.PaymentRequestDTO;
import com.example.newView.model.dto.PaymentResponseDTO;
import com.example.newView.model.entity.Order;
import com.example.newView.model.entity.OrderStatus;
import com.example.newView.model.entity.Payment;
import com.example.newView.model.entity.PaymentStatus;
import com.example.newView.repository.OrderRepository;
import com.example.newView.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.newView.exception.PaymentProcessingException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponseDTO processPayment(Long orderId, PaymentRequestDTO paymentDetails) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Validate order status
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOperationException("Order is not in PENDING status");
        }

        // Create payment record
        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .lastFourDigits(getLastFourDigits(paymentDetails.getCardNumber()))
                .cardType(determineCardType(paymentDetails.getCardNumber()))
                .build();

        // Process payment (simulate payment gateway integration)
        try {
            String transactionId = processPaymentWithGateway(paymentDetails, order.getTotalAmount());
            payment.setTransactionId(transactionId);
            payment.setStatus(PaymentStatus.COMPLETED);

            // Update order status
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            throw new PaymentProcessingException("Payment processing failed: " + e.getMessage());
        }

        payment = paymentRepository.save(payment);
        return createPaymentResponse(payment);
    }

    public PaymentResponseDTO getPaymentDetails(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        return createPaymentResponse(payment);
    }

    @Transactional
    public PaymentResponseDTO refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new InvalidOperationException("Payment cannot be refunded");
        }

        // Process refund (simulate refund process)
        try {
            processRefundWithGateway(payment.getTransactionId());
            payment.setStatus(PaymentStatus.REFUNDED);
            payment = paymentRepository.save(payment);

            // Update order status
            Order order = payment.getOrder();
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        } catch (Exception e) {
            throw new PaymentProcessingException("Refund processing failed: " + e.getMessage());
        }

        return createPaymentResponse(payment);
    }

    // Helper methods
    private String processPaymentWithGateway(PaymentRequestDTO paymentDetails, Double amount) {
        // Simulate payment processing
        try {
            Thread.sleep(1000);
            return "TXN_" + System.currentTimeMillis(); // Simulate transaction ID
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PaymentProcessingException("Payment processing interrupted");
        }
    }

    private void processRefundWithGateway(String transactionId) {
        // Simulate refund processing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PaymentProcessingException("Refund processing interrupted");
        }
    }

    private String getLastFourDigits(String cardNumber) {
        return cardNumber.substring(cardNumber.length() - 4);
    }

    private String determineCardType(String cardNumber) {
        // Simple card type determination based on first digit
        char firstDigit = cardNumber.charAt(0);
        return switch (firstDigit) {
            case '4' -> "VISA";
            case '5' -> "MASTERCARD";
            case '3' -> "AMEX";
            default -> "UNKNOWN";
        };
    }

    private PaymentResponseDTO createPaymentResponse(Payment payment) {
        return PaymentResponseDTO.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .paymentDate(payment.getPaymentDate())
                .cardLastFourDigits(payment.getLastFourDigits())
                .cardType(payment.getCardType())
                .build();
    }
}
