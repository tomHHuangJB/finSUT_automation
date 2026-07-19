package com.example.crdtests.model;

import java.math.BigDecimal;

public record OrderRow(
        String externalOrderId,
        String side,
        String orderType,
        BigDecimal quantity,
        BigDecimal filledQuantity,
        BigDecimal remainingQuantity,
        String status,
        String createdBy) {
}
