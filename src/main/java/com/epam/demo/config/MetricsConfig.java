package com.epam.demo.config;

import com.epam.demo.repository.ProductRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Gauge productCountGauge(ProductRepository productRepository, MeterRegistry meterRegistry) {
        return Gauge.builder("products.total", productRepository, ProductRepository::count)
                .description("Total number of products in database")
                .register(meterRegistry);
    }
}
