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
                .path("/friendships/**")
                .uri(friendshipServiceUrl)
            )
            .build();
    }

    @Bean
    public RouteLocator friendshipServiceApiDocs(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/docs/friendshipservice/v3/api-docs")
                .filters(f -> f
                    .rewritePath("/docs/friendshipservice/v3/api-docs", "/v3/api-docs")
                )
                .uri(friendshipServiceUrl)
            )
            .build();
    }
}
