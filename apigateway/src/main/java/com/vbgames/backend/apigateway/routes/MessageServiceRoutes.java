package com.vbgames.backend.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageServiceRoutes {

    @Value("${microservices.message-service.url}")
    private String messageServiceUrl;

    @Bean
    RouteLocator messageRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/messages/**")
                .uri(messageServiceUrl)
            )
            .build();
    }

    @Bean
    public RouteLocator messageServiceApiDocs(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/docs/messageservice/v3/api-docs")
                .filters(f -> f
                    .rewritePath("/docs/messageservice/v3/api-docs", "/v3/api-docs")
                )
                .uri(messageServiceUrl)
            )
            .build();
    }
}
