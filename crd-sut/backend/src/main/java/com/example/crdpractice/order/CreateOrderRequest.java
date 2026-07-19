package com.example.crdpractice.order;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderRequest(
        @NotBlank String externalOrderId,
        @NotNull UUID accountId,
        @NotNull UUID securityId,
        @NotNull OrderSide side,
        @NotNull OrderType orderType,
        @NotNull @DecimalMin(value = "0.0001", message = "must be greater than zero") BigDecimal quantity,
        @DecimalMin(value = "0.0000", message = "must be nonnegative") BigDecimal limitPrice) {
}
