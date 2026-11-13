package com.example.reactive.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SportRouter {

    @Bean
    RouterFunction<ServerResponse> sportRouters(SportHandler handler) {
        return route(POST("/api/v1/sport/{sportname}"), handler::create)
                .andRoute(GET("/api/v1/sport"), handler::search);
    }
}
