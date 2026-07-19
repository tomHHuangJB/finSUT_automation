package com.example.crdpractice.security;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

record SecurityResponse(
        UUID id,
        String symbol,
        String name,
        String assetClass,
        String currency,
        BigDecimal currentPrice,
        Instant priceTimestamp,
        boolean restricted,
        boolean active,
        Instant createdAt,
        Instant updatedAt) {

    static SecurityResponse from(SecurityEntity security) {
        return new SecurityResponse(
                security.getId(),
                security.getSymbol(),
                security.getName(),
                security.getAssetClass(),
                security.getCurrency(),
                security.getCurrentPrice(),
                security.getPriceTimestamp(),
                security.isRestricted(),
                security.isActive(),
                security.getCreatedAt(),
                security.getUpdatedAt());
    }
}
