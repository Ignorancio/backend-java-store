package com.example.store.auth.infrastructure;

import com.example.store.auth.application.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Services", description = "Operations related to authentication")
public class AuthController {

    private final AuthService authService;
    @Value("${application.security.jwt.access-token-expiration:900000}") // 15 min
    private long accessTokenDuration;

    @Value("${application.security.jwt.refresh-token-expiration:604800000}") // 7 días
    private long refreshTokenDuration;


    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        final TokenResponse response = authService.register(request);
        return setCookieAndResponse(response);
    }

    @PostMapping("/register/admin")
    @Operation(summary = "Register a new admin only for development purposes")
    public ResponseEntity<Void> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        final TokenResponse response = authService.registerAdmin(request);
        return setCookieAndResponse(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user")
    public ResponseEntity<Void> authenticate(@Valid @RequestBody AuthRequest request) {
        final TokenResponse response = authService.authenticate(request);
        return setCookieAndResponse(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh a token")
    public ResponseEntity<Void> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication) {
        final TokenResponse response = authService.refreshToken(authentication);
        return setCookieAndResponse(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user and clear cookies")
    public ResponseEntity<Void> logout() {
        ResponseCookie jwtCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(false) // Cambiar a false si estás en localhost sin HTTPS
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/") // O restringir a "/auth/refresh"
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }

    private ResponseEntity<Void> setCookieAndResponse(TokenResponse response) {
        ResponseCookie jwtCookie = ResponseCookie.from("access_token", response.accessToken())
                .httpOnly(true)
                .secure(false) //cambiar al desplegar
                .path("/")
                .maxAge(accessTokenDuration / 1000)
                .sameSite("Strict")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", response.accessToken())
                .httpOnly(true)
                .secure(false) //cambiar al desplegar
                .path("/")
                .maxAge(refreshTokenDuration / 1000)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }
}
