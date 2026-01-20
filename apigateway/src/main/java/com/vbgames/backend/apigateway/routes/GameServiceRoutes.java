package com.vbgames.backend.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameServiceRoutes {

    @Value("${microservices.game-service.url}")
    private String gameServiceUrl;

    @Bean
    RouteLocator gameRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/games/**")
                .uri(gameServiceUrl)
            )
            .build();
    }

    @Bean
    public RouteLocator gameServiceApiDocs(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/docs/gameservice/v3/api-docs")
                .filters(f -> f
                    .rewritePath("/docs/gameservice/v3/api-docs", "/v3/api-docs")
                )
                .uri(gameServiceUrl)
            )
            .build();
    }
}
