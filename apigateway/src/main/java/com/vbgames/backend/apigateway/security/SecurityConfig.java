package com.vbgames.backend.apigateway.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

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
                .pathMatchers(HttpMethod.POST,"/auth/login", "/auth/refresh", "/auth/register").permitAll()
                .pathMatchers(HttpMethod.GET,"/games", "/auth/verify/*").permitAll()
                .pathMatchers(excludedUrls).permitAll()
                // Rutas solo para el admin
                .pathMatchers(HttpMethod.POST,"/games").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT,"/games/*").hasRole("ADMIN")
                .pathMatchers(HttpMethod.POST,"/products/*").hasRole("ADMIN")

                // .pathMatchers("/ws/**").permitAll()

                .anyExchange().authenticated()
            )
            .addFilterAt(new JwtAccessTokenValidationFilter(jwtService), SecurityWebFiltersOrder.AUTHENTICATION)
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((exchange, e) -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                })
            )
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            String path = request.getRequest().getURI().getPath();

            // Excluyo las rutas de websockets
            if (path.startsWith("/ws/")) {
                return null;
            }
            
            CorsConfiguration config = new CorsConfiguration();

            config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://localhost:4200",
                "https://virtual-board-games.web.app"
            ));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);

            return config;
        };
    }
}