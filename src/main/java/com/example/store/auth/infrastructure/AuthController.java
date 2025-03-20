package com.example.store.auth.infrastructure;

import com.example.store.auth.application.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Services", description = "Operations related to authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        final TokenResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/admin")
    @Operation(summary = "Register a new admin only for development purposes")
    public ResponseEntity<TokenResponse> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        final TokenResponse response = authService.registerAdmin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user")
    public ResponseEntity<TokenResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        final TokenResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh a token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication) {
        final TokenResponse response = authService.refreshToken(authentication);
        return ResponseEntity.ok(response);
    }
}
