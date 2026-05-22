package com.vbgames.backend.apigateway.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class JwtAccessTokenValidationFilter implements WebFilter{

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String token = jwtService.getAccessToken(request.getCookies());

        if(token == null) return chain.filter(exchange);

        try {
            Claims claims = jwtService.verifyAccessToken(token);

            String userId = claims.getSubject();

            // Saco las authorities y las convierto en String verificando que sean Strings 
            Object rolesObj = claims.get("authorities", List.class);
            List<String> roles = new ArrayList<>();

            if(rolesObj instanceof List<?>) {
                for (Object role : (List<?>) rolesObj) {
                    roles.add((String) role);
                }
            }

            List<SimpleGrantedAuthority> authorities = roles
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);

            // Modifico el request para añadir un header con el user id
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build())
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

        } catch (JwtException e) {
            return chain.filter(exchange);
        }     
    }
}