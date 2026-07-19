package com.example.crdpractice.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "external_order_id", nullable = false, unique = true)
    private String externalOrderId;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "security_id", nullable = false)
    private UUID securityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderSide side;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(name = "limit_price")
    private BigDecimal limitPrice;

    @Column(name = "filled_quantity", nullable = false)
    private BigDecimal filledQuantity;

    @Column(name = "remaining_quantity", nullable = false)
    private BigDecimal remainingQuantity;

    @Column(name = "average_execution_price")
    private BigDecimal averageExecutionPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "released_at")
    private Instant releasedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Version
    private Long version;

    protected OrderEntity() {
    }

    static OrderEntity create(String externalOrderId, UUID accountId, UUID securityId, OrderSide side, OrderType orderType, BigDecimal quantity, BigDecimal limitPrice, String createdBy) {
        OrderEntity order = new OrderEntity();
        order.externalOrderId = externalOrderId;
        order.accountId = accountId;
        order.securityId = securityId;
        order.side = side;
        order.orderType = orderType;
        order.quantity = quantity;
        order.limitPrice = limitPrice;
        order.filledQuantity = BigDecimal.ZERO;
        order.remainingQuantity = quantity;
        order.status = OrderStatus.DRAFT;
        order.createdBy = createdBy;
        order.createdAt = Instant.now();
        return order;
    }

    UUID getId() {
        return id;
    }

    String getExternalOrderId() {
        return externalOrderId;
    }

    UUID getAccountId() {
        return accountId;
    }

    UUID getSecurityId() {
        return securityId;
    }

    OrderSide getSide() {
        return side;
    }

    OrderType getOrderType() {
        return orderType;
    }

    BigDecimal getQuantity() {
        return quantity;
    }

    BigDecimal getLimitPrice() {
        return limitPrice;
    }

    BigDecimal getFilledQuantity() {
        return filledQuantity;
    }

    BigDecimal getRemainingQuantity() {
        return remainingQuantity;
    }

    BigDecimal getAverageExecutionPrice() {
        return averageExecutionPrice;
    }

    OrderStatus getStatus() {
        return status;
    }

    String getCreatedBy() {
        return createdBy;
    }

    String getApprovedBy() {
        return approvedBy;
    }

    Instant getCreatedAt() {
        return createdAt;
    }

    Instant getApprovedAt() {
        return approvedAt;
    }

    Instant getReleasedAt() {
        return releasedAt;
    }

    Instant getCompletedAt() {
        return completedAt;
    }

    Long getVersion() {
        return version;
    }
}
