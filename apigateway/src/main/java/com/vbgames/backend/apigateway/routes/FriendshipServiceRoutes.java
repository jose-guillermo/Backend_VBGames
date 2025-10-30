package com.vbgames.backend.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FriendshipServiceRoutes {

    @Value("${microservices.friendship-service.url}")
    private String friendshipServiceUrl;

    @Bean
    RouteLocator friendshipRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                    .path("/friendship-service/**")
                    .uri(friendshipServiceUrl)
                )
                .build();
    }
}
