package com.example.crdpractice.health;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
class HealthController {

    @GetMapping
    Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "service", "crd-practice-backend",
                "timestamp", Instant.now().toString());
    }
}
