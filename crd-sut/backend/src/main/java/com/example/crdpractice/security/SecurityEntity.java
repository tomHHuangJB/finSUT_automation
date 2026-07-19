package com.example.crdpractice.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "securities")
public class SecurityEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String name;

    @Column(name = "asset_class", nullable = false)
    private String assetClass;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;

    @Column(name = "price_timestamp", nullable = false)
    private Instant priceTimestamp;

    @Column(nullable = false)
    private boolean restricted;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected SecurityEntity() {
    }

    public UUID getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getAssetClass() {
        return assetClass;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public Instant getPriceTimestamp() {
        return priceTimestamp;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
