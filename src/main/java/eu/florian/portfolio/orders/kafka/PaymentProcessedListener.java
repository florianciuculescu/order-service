package eu.florian.portfolio.orders.kafka;

import eu.florian.portfolio.contracts.PaymentProcessedEvent;
import eu.florian.portfolio.contracts.PaymentStatus;
import eu.florian.portfolio.orders.domain.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class PaymentProcessedListener {

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;

    public PaymentProcessedListener(ObjectMapper objectMapper, OrderRepository orderRepository) {
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @KafkaListener(topics = "${app.kafka.topics.paymentProcessed}")
    public void onMessage(byte[] payload) {
        try {
            PaymentProcessedEvent event = objectMapper.readValue(payload, PaymentProcessedEvent.class);

            orderRepository.findById(event.orderId()).ifPresent(order -> {
                if (event.status() == PaymentStatus.SUCCEEDED) {
                    order.markPaid();
                } else {
                    order.markPaymentFailed();
                }
            });

        } catch (Exception e) {
            throw new IllegalStateException("Failed to process PaymentProcessedEvent", e);
        }
    }
}
