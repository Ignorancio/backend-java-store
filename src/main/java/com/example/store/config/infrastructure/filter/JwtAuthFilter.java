package com.example.store.config.infrastructure.filter;

import com.example.store.user.domain.User;
import com.example.store.user.infrastructure.entity.UserEntity;
import com.example.store.user.infrastructure.mapper.UserMapper;
import com.example.store.user.infrastructure.repository.QueryUserRepository;
import com.example.store.config.application.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final QueryUserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try{
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            final String jwt = authHeader.substring(7);
            final String id = jwtService.extractSubject(jwt);
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (id == null || authentication != null) {
                filterChain.doFilter(request, response);
                return;
            }
            final UserEntity user = userRepository.findById(UUID.fromString(id)).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));

            final User user1 = userMapper.userEntityToUser(user);

            final boolean isTokenValid = jwtService.isTokenValid(jwt, user1);

            if (isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    "{ \"error\": \"Unauthorized\", \"message\": \"Token expirado\" }"
            );
        }
        catch (SignatureException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    "{ \"error\": \"Unauthorized\", \"message\": \"Token invalido\" }"
            );
        }
        catch (MalformedJwtException e)
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    "{ \"error\": \"Unauthorized\", \"message\": \"Token mal formado\" }"
            );
        }

    }
}
