package com.example.newView.controller;

import com.example.newView.model.dto.*;
import com.example.newView.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        System.out.println("Registration request received for username: " + request.getUsername());
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(@Valid @RequestBody AuthRequestDTO request) {
        System.out.println("Authentication request received for username: " + request.getUsername());
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")  // Only existing admins can create new admins
    public ResponseEntity<AuthResponseDTO> registerAdmin(@Valid @RequestBody RegisterRequestDTO request) {
        System.out.println("Admin registration request received for username: " + request.getUsername());
        return ResponseEntity.ok(authenticationService.registerAdmin(request));
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid ChangePasswordRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("Password change request received for user: " + userDetails.getUsername());
        authenticationService.changePassword(
                userDetails.getUsername(),
                request.getOldPassword(),
                request.getNewPassword()
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        System.out.println("Password reset request received for email: " + request.getEmail());
        authenticationService.resetPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }
}
