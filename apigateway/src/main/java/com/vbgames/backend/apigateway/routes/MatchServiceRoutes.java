package com.vbgames.backend.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MatchServiceRoutes {
    @Value("${microservices.match-service.url}")
    private String matchServiceUrl;

    @Bean
    RouteLocator matchRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/matches/**")
                .uri(matchServiceUrl)
            )
            .build();
    }

    @Bean
    public RouteLocator matchServiceApiDocs(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/docs/matchservice/v3/api-docs")
                .filters(f -> f
                    .rewritePath("/docs/matchservice/v3/api-docs", "/v3/api-docs")
                )
                .uri(matchServiceUrl)
            )
            .build();
    }
}
