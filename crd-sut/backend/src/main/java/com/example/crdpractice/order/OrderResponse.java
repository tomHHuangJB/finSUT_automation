package com.example.crdpractice.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String externalOrderId,
        UUID accountId,
        UUID securityId,
        OrderSide side,
        OrderType orderType,
        BigDecimal quantity,
        BigDecimal limitPrice,
        BigDecimal filledQuantity,
        BigDecimal remainingQuantity,
        BigDecimal averageExecutionPrice,
        OrderStatus status,
        String createdBy,
        String approvedBy,
        Instant createdAt,
        Instant approvedAt,
        Instant releasedAt,
        Instant completedAt,
        Long version) {

    static OrderResponse from(OrderEntity order) {
        return new OrderResponse(
                order.getId(),
                order.getExternalOrderId(),
                order.getAccountId(),
                order.getSecurityId(),
                order.getSide(),
                order.getOrderType(),
                order.getQuantity(),
                order.getLimitPrice(),
                order.getFilledQuantity(),
                order.getRemainingQuantity(),
                order.getAverageExecutionPrice(),
                order.getStatus(),
                order.getCreatedBy(),
                order.getApprovedBy(),
                order.getCreatedAt(),
                order.getApprovedAt(),
                order.getReleasedAt(),
                order.getCompletedAt(),
                order.getVersion());
    }
}
