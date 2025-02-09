package com.example.store.service;

import com.example.store.dtos.AuthRequest;
import com.example.store.dtos.RegisterRequest;
import com.example.store.dtos.TokenResponse;
import com.example.store.model.Role;
import com.example.store.model.User;
import com.example.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(final RegisterRequest request) {
        final User userExists = userRepository.findByEmail(request.email()).orElse(null);
        if(userExists != null) {
            throw new IllegalArgumentException("User already exists");
        }
        final User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        final User savedUser = userRepository.save(user);
        final String jwtToken = jwtService.generateToken(savedUser);
        final String refreshToken = jwtService.generateRefreshToken(savedUser);

        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse authenticate(final AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        final User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        final String accessToken = jwtService.generateToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshToken(@NonNull final String authentication) {
        if (!authentication.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid auth header");
        }
        final String refreshToken = authentication.substring(7);
        final String username = jwtService.extractSubject(refreshToken);
        if(username == null) {
            return null;
        }
        final User user = this.userRepository.findByEmail(username).orElseThrow();
        final boolean isTokenValid = jwtService.isTokenValid(refreshToken, user);
        if (!isTokenValid) {
            return null;
        }

        final String accessToken = jwtService.generateRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken);
    }
}
