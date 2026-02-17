package eu.florian.portfolio.orders.api;

import eu.florian.portfolio.contracts.OrderCreatedEvent;
import eu.florian.portfolio.orders.domain.OrderEntity;
import eu.florian.portfolio.orders.domain.OrderRepository;
import eu.florian.portfolio.orders.domain.OrderStatus;
import eu.florian.portfolio.orders.kafka.OrderEventPublisher;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher publisher;

    public OrderController(OrderRepository orderRepository, OrderEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public CreatedOrderResponse create(@RequestBody @Valid CreateOrderRequest request) {
        UUID orderId = UUID.randomUUID();

        OrderEntity entity = new OrderEntity(
                orderId,
                request.amount(),
                request.currency(),
                OrderStatus.CREATED,
                Instant.now());

        orderRepository.save(entity);

        publisher.publish(new OrderCreatedEvent(
                UUID.randomUUID(),
                orderId,
                entity.getAmount(),
                entity.getCurrency(),
                entity.getCreatedAt()
        ));

        return new CreatedOrderResponse(orderId);
    }

    public record CreatedOrderResponse(UUID orderId) {}
}
