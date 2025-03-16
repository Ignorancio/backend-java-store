package com.example.store.auth.application;

import com.example.store.auth.infrastructure.AuthRequest;
import com.example.store.auth.infrastructure.RegisterRequest;
import com.example.store.auth.infrastructure.TokenResponse;
import com.example.store.user.domain.Role;
import com.example.store.user.infrastructure.entity.UserEntity;
import com.example.store.user.infrastructure.repository.QueryUserRepository;
import com.example.store.config.application.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final QueryUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(final RegisterRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Usuario ya existe");
        }
        final UserEntity user = UserEntity.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(Role.USER))
                .build();

        final UserEntity savedUser = userRepository.save(user);
        final String jwtToken = jwtService.generateToken(savedUser);
        final String refreshToken = jwtService.generateRefreshToken(savedUser);

        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse authenticate(final AuthRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        }catch (AuthenticationException e){
            throw new IllegalArgumentException("Credenciales invalidas");
        }
        final UserEntity user = userRepository.findByEmail(request.email())
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
        final UserEntity user = this.userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        final boolean isTokenValid = jwtService.isTokenValid(refreshToken, user);
        if (!isTokenValid) {
            throw new IllegalArgumentException("Invalid token");
        }

        final String accessToken = jwtService.generateRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse registerAdmin(final RegisterRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Usuario ya existe");
        }
        final UserEntity user = UserEntity.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(Role.USER, Role.ADMIN))
                .build();

        final UserEntity savedUser = userRepository.save(user);
        final String jwtToken = jwtService.generateToken(savedUser);
        final String refreshToken = jwtService.generateRefreshToken(savedUser);

        return new TokenResponse(jwtToken, refreshToken);
    }
}
