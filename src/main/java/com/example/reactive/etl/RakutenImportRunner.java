package com.example.reactive.etl;

import com.example.reactive.entity.Sport;
import com.example.reactive.repository.SportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import tools.jackson.databind.JsonNode;

@Component
public class RakutenImportRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(RakutenImportRunner.class);

    private final WebClient webClient;
    private final SportRepository repository;

    @Value("${rakuten.application-id:}")
    private String applicationId;

    @Value("${rakuten.keyword:スポーツ}")
    private String keyword;

    public RakutenImportRunner(WebClient.Builder webClientBuilder,
                               SportRepository repository,
                               @Value("${rakuten.base-url:https://app.rakuten.co.jp}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (applicationId.isBlank()) {
            log.info("Rakuten import skipped: set RAKUTEN_APPLICATION_ID to enable it");
            return;
        }

        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/services/api/IchibaItem/Search/20170706")
                        .queryParam("applicationId", applicationId)
                        .queryParam("format", "json")
                        .queryParam("keyword", keyword)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMapMany(this::toSports)
                .distinct(Sport::getName)
                .concatMap(repository::save)
                .then()
                .subscribe(
                        unused -> { },
                        error -> log.error("Rakuten import failed", error),
                        () -> log.info("Rakuten import completed")
                );
    }

    private Flux<Sport> toSports(JsonNode response) {
        JsonNode items = response.path("Items");

        if (!items.isArray()) {
            return Flux.error(new IllegalStateException(
                    "Rakuten response does not contain an Items array"));
        }

        return Flux.fromIterable(items)
                .map(item -> item.path("Item"))
                .filter(item -> item.hasNonNull("genreId")
                        && item.hasNonNull("genreName"))
                .map(item -> new Sport(
                        null,
                        item.path("genreName").asString()
                ));
    }
}