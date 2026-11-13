package com.example.reactive.api;

import com.example.reactive.entity.Sport;
import com.example.reactive.repository.SportRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SportHandler {
    private final SportRepository repository;

    public SportHandler(SportRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        String name = request.pathVariable("sportname").trim();

        if (name.isBlank()) {
            return ServerResponse.badRequest().bodyValue("Sport name cannot be empty");
        }

        return repository.save(new Sport(null, name))
                .flatMap(savedSport -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedSport))
                .onErrorResume(DuplicateKeyException.class, error -> ServerResponse
                        .status(409)
                        .bodyValue("Sport with name '" + name + "' already exists"));

    }

    public Mono<ServerResponse> search(ServerRequest serverRequest) {
        String query = serverRequest.queryParam("q").orElse("").trim();

        if (query.isBlank()) {
            return ServerResponse.badRequest().bodyValue("Query parameter 'q' cannot be empty");
        }

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(repository.searchByName("%" + query + "%"), Sport.class);
    }
}
