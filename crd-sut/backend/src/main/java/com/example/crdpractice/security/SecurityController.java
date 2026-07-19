package com.example.crdpractice.security;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/securities")
class SecurityController {

    private final SecurityRepository securityRepository;

    SecurityController(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    @GetMapping
    List<SecurityResponse> listSecurities() {
        return securityRepository.findAll()
                .stream()
                .map(SecurityResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    ResponseEntity<SecurityResponse> getSecurity(@PathVariable UUID id) {
        return securityRepository.findById(id)
                .map(SecurityResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
