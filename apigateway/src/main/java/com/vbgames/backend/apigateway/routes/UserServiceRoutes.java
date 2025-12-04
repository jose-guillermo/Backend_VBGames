package com.vbgames.backend.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceRoutes {

    @Value("${microservices.user-service.url}")
    private String userServiceUrl;

    @Bean
    RouteLocator userRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/users/**")
                .uri(userServiceUrl)
            )
            // .route(r -> r
            //     .path("/login")
            //     .uri(userServiceUrl)
            // )
            // .route(r -> r
            //     .path("/refresh")
            //     .uri(userServiceUrl)
            // )
            .build();
    }
}
