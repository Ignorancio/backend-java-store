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

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(final RegisterRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Usuario ya existe");
        }
        final User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(Role.USER))
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
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
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
            throw new IllegalArgumentException("Invalid token");
        }
        final User user = this.userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        final boolean isTokenValid = jwtService.isTokenValid(refreshToken, user);
        if (!isTokenValid) {
            throw new IllegalArgumentException("Invalid token");
        }

        final String accessToken = jwtService.generateRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken);
    }
}
