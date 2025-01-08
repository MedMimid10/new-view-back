package com.example.newView.service;

import com.example.newView.exception.InvalidOperationException;
import com.example.newView.exception.ResourceNotFoundException;
import com.example.newView.exception.UnauthorizedAccessException;
import com.example.newView.model.dto.CartItemRequestDTO;
import com.example.newView.model.dto.CartItemResponseDTO;
import com.example.newView.model.entity.CartItem;
import com.example.newView.model.entity.Product;
import com.example.newView.model.entity.User;
import com.example.newView.repository.CartItemRepository;
import com.example.newView.repository.ProductRepository;
import com.example.newView.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartItemResponseDTO addToCart(String username, CartItemRequestDTO request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new InvalidOperationException("Not enough stock available");
        }

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
                .orElse(CartItem.builder()
                        .user(user)
                        .product(product)
                        .build());

        cartItem.setQuantity(request.getQuantity());
        cartItem.setPrice(product.getPrice());

        cartItem = cartItemRepository.save(cartItem);
        return createCartItemResponse(cartItem);
    }

    public List<CartItemResponseDTO> getCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return cartItemRepository.findByUser(user).stream()
                .map(this::createCartItemResponse)
                .collect(Collectors.toList());
    }

    public void removeFromCart(String username, Long cartItemId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Not authorized to modify this cart item");
        }

        cartItemRepository.delete(cartItem);
    }

    private CartItemResponseDTO createCartItemResponse(CartItem cartItem) {
        return CartItemResponseDTO.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .total(cartItem.getPrice() * cartItem.getQuantity())
                .build();
    }

    @Transactional
    public void clearCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        cartItemRepository.deleteByUser(user);
    }
}
