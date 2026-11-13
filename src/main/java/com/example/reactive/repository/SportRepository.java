package com.example.reactive.repository;

import com.example.reactive.entity.Sport;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SportRepository extends ReactiveCrudRepository<Sport, Integer> {

    @Query("""
            SELECT id, name
            FROM sports
            WHERE LOWER(name) LIKE LOWER(:pattern)
            ORDER BY name
            """)
    Flux<Sport> searchByName(@Param("pattern") String name);
}
