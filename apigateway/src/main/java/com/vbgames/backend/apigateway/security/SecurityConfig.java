package com.vbgames.backend.apigateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;

    @Value("${security.excluded.urls}")
    private String[] excludedUrls;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .authorizeExchange(auth -> auth
                // Rutas públicas
                .pathMatchers(HttpMethod.POST,"auth/login", "auth/refresh", "auth/register").permitAll()
                .pathMatchers(HttpMethod.GET,"games", "auth/verify/*").permitAll()
                .pathMatchers(excludedUrls).permitAll()
                // Rutas solo para el admin
                .pathMatchers(HttpMethod.POST,"games").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT,"games/*").hasRole("ADMIN")
                .pathMatchers(HttpMethod.POST,"products/*").hasRole("ADMIN")

                .anyExchange().authenticated()
            )
            .addFilterBefore(new JwtRefreshTokenValidationFIlter(jwtService),  SecurityWebFiltersOrder.FIRST)
            .addFilterAt(new JwtAccessTokenValidationFilter(jwtService), SecurityWebFiltersOrder.AUTHENTICATION)
            .csrf(csrf -> csrf.disable())
            .build();
    }
}