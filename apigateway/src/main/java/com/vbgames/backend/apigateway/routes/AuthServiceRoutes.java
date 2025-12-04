package com.vbgames.backend.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthServiceRoutes {
    
    @Value("${microservices.auth-service.url}")
    private String authServiceUrl;

    @Bean
    RouteLocator authRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/auth/login")
                .filters(f -> f.rewritePath("/auth/login", "/login"))
                .uri(authServiceUrl)
            )
            .route(r -> r
                .path("/auth/refresh")
                .filters(f -> f.rewritePath("/auth/refresh", "/refresh"))
                .uri(authServiceUrl)
            )
            .route(r -> r
                .path("/auth/**")
                .uri(authServiceUrl)
            )
            .build();
    }
}
