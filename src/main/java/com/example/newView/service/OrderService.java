package com.example.newView.service;

import com.example.newView.exception.InvalidOperationException;
import com.example.newView.exception.ResourceNotFoundException;
import com.example.newView.model.dto.CartItemResponseDTO;
import com.example.newView.model.dto.OrderItemDTO;
import com.example.newView.model.dto.OrderResponseDTO;
import com.example.newView.model.entity.*;
import com.example.newView.repository.OrderItemRepository;
import com.example.newView.repository.OrderRepository;
import com.example.newView.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final PaymentService paymentService;


    public OrderResponseDTO createOrder(String username) {
        List<CartItemResponseDTO> cartItems = cartService.getCart(username);
        if(cartItems.isEmpty()) {
            throw new InvalidOperationException("Cart is empty");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .orderDate(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItemResponseDTO item : cartItems) {
            Product product = productService.updateStock(item.getProductId(), -item.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .build();

            orderItems.add(orderItem);
            total += item.getTotal();
        }

        order.setTotalAmount(total);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(username);

        return mapToOrderResponse(savedOrder);
    }

    public List<OrderResponseDTO> getUserOrders(String username) {
        System.out.println("Service: Fetching orders for user: " + username);
        return orderRepository.findByUserUsername(username).stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO getOrderById(Long orderId, String username) {
        System.out.println("Service: Fetching order: " + orderId);
        Order order = orderRepository.findByIdAndUserUsername(orderId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapToOrderResponse(order);
    }

    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
        System.out.println("Service: Updating order status: " + orderId + " to " + newStatus);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        validateStatusTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);

        switch(newStatus) {
            case CONFIRMED -> order.setConfirmedDate(LocalDateTime.now());
            case SHIPPED -> order.setShippedDate(LocalDateTime.now());
            case DELIVERED -> order.setDeliveredDate(LocalDateTime.now());
            case CANCELLED -> handleOrderCancellation(order);
        }

        return mapToOrderResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponseDTO cancelOrder(Long orderId, String username) {
        Order order = orderRepository.findByIdAndUserUsername(orderId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new InvalidOperationException("Cannot cancel order in current status");
        }

        return updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }
    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        switch(currentStatus) {
            case PENDING:
                if(newStatus != OrderStatus.CONFIRMED && newStatus != OrderStatus.CANCELLED) {
                    throw new InvalidOperationException("Invalid status transition");
                }
                break;
            case CONFIRMED:
                if(newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELLED) {
                    throw new InvalidOperationException("Invalid status transition");
                }
                break;
            case SHIPPED:
                if(newStatus != OrderStatus.DELIVERED) {
                    throw new InvalidOperationException("Invalid status transition");
                }
                break;
            case DELIVERED:
            case CANCELLED:
                throw new InvalidOperationException("Cannot change status of " +
                        currentStatus + " order");
        }
    }

    private void handleOrderCancellation(Order order) {
        // Restore product quantities using existing updateStock method
        order.getItems().forEach(item -> {
            // Add back the quantity (that's why we use positive value)
            productService.updateStock(item.getProduct().getId(), item.getQuantity());
        });

        // If payment exists, initiate refund
        if(order.getPayment() != null &&
                order.getPayment().getStatus() == PaymentStatus.COMPLETED) {
            paymentService.refundPayment(order.getPayment().getId());
        }
    }

    private OrderResponseDTO mapToOrderResponse(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .username(order.getUser().getUsername())
                .items(order.getItems().stream()
                        .map(this::mapToOrderItemDTO)
                        .collect(Collectors.toList()))
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .confirmedDate(order.getConfirmedDate())
                .shippedDate(order.getShippedDate())
                .deliveredDate(order.getDeliveredDate())
                .trackingNumber(order.getTrackingNumber())
                .paymentStatus(order.getPayment() != null ?
                        order.getPayment().getStatus() : null)
                .build();
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItem item) {
        return OrderItemDTO.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .total(item.getPrice() * item.getQuantity())
                .build();
    }

}
