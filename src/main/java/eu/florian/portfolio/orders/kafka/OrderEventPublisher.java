package eu.florian.portfolio.orders.kafka;

import tools.jackson.databind.ObjectMapper;
import eu.florian.portfolio.contracts.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final String topic;

    public OrderEventPublisher(
            KafkaTemplate<String, byte[]> kafkaTemplate,
            tools.jackson.databind.ObjectMapper objectMapper,
            @Value("${app.kafka.topics.orderCreated}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    public void publish(OrderCreatedEvent event) {
        try {
            byte[] payload = objectMapper.writeValueAsBytes(event);
            kafkaTemplate.send(topic, event.orderId().toString(), payload);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to publish OrderCreatedEvent", e);
        }
    }
}
