package com.vbgames.backend.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductServiceRoutes {

    @Value("${microservices.product-service.url}")
    private String productServiceUrl;

    @Bean
    RouteLocator productRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/products/**")
                .uri(productServiceUrl)
            )
            .build();
    }
}
