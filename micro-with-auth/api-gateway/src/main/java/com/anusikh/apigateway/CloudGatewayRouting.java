package com.anusikh.apigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anusikh.apigateway.filter.MyGatewayFilter;

@Configuration
public class CloudGatewayRouting {

    @Autowired
    private MyGatewayFilter filter;

    // NOTE:
    // https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#fluent-java-routes-api
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("au", r -> r.path("/au/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))
                .build();
    }
}
