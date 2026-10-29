package com.epam.demo.health;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class AppHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        boolean appIsHealthy = true;

        if (!appIsHealthy) {
            return Health.down()
                    .withDetail("service", "Application is unhealthy")
                    .build();
        }

        return Health.up()
                .withDetail("service", "Application is healthy")
                .withDetail("status", "OK")
                .build();
    }
}
