package com.example.newView.controller;

import com.example.newView.model.dto.CartItemRequestDTO;
import com.example.newView.model.dto.CartItemResponseDTO;
import com.example.newView.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /**
     * Add item to user's cart
     * We use UserDetails instead of User because:
     * 1. It's provided directly by Spring Security's authentication context
     * 2. More lightweight than loading full User entity
     * 3. Follows security best practices by using only what's needed
     */
    @PostMapping
    public ResponseEntity<CartItemResponseDTO> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CartItemRequestDTO request) {
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUsername(), request));
    }

    /**
     * Get current user's cart contents
     * Even with secured endpoints, using UserDetails is preferred because:
     * 1. Guaranteed to have authenticated user info
     * 2. No need for additional database query to load User
     * 3. Consistent with Spring Security patterns
     */
    @GetMapping
    public ResponseEntity<List<CartItemResponseDTO>> getCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCart(userDetails.getUsername()));
    }

    /**
     * Remove item from user's cart
     * @param cartItemId ID of the cart item to remove
     */
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long cartItemId) {
        cartService.removeFromCart(userDetails.getUsername(), cartItemId);
        return ResponseEntity.noContent().build();
    }
}
