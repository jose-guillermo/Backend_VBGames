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

    @Bean
    public RouteLocator productServiceApiDocs(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/docs/productservice/v3/api-docs")
                .filters(f -> f
                    .rewritePath("/docs/productservice/v3/api-docs", "/v3/api-docs")
                )
                .uri(productServiceUrl)
            )
            .build();
    }
}
