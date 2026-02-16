package eu.florian.portfolio.contracts.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        UUID orderId,
        BigDecimal amount,
        String currency,
        Instant createdAt
) {}