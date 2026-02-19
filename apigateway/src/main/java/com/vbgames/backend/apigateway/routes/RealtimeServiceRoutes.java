package com.vbgames.backend.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RealtimeServiceRoutes {

    @Value("${microservices.realtime-service.url}")
    private String realtimeServiceUrl;

    @Bean
    RouteLocator realtimeRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/ws/**")
                .uri(realtimeServiceUrl)
            )
            .build();
    }
}
