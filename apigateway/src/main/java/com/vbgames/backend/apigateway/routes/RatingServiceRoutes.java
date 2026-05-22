package com.vbgames.backend.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RatingServiceRoutes {

    @Value("${microservices.rating-service.url}")
    private String ratingServiceUrl;

    @Bean
    RouteLocator ratingRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/ratings/**")
                .uri(ratingServiceUrl)
            )
            .build();
    }

    @Bean
    public RouteLocator ratingServiceApiDocs(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/docs/ratingservice/v3/api-docs")
                .filters(f -> f
                    .rewritePath("/docs/ratingservice/v3/api-docs", "/v3/api-docs")
                )
                .uri(ratingServiceUrl)
            )
            .build();
    }
}
