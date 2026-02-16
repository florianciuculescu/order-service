package eu.florian.portfolio.orders.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateOrderRequest(
        @NotNull BigDecimal amount,
        @NotBlank String currency
) {}
