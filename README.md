# order-service

Simple Spring Boot service that:
- exposes a REST endpoint for creating orders
- persists orders in PostgreSQL
- publishes an `OrderCreatedEvent` to Kafka

---

## Tech stack
- Java 17+
- Spring Boot 4
- Spring WebMVC
- Spring Data JPA + Hibernate
- Flyway
- PostgreSQL
- Kafka (producer)
- Jackson

---

## API

### Create order
`POST /api/orders`

Request:
```json
{
  "amount": 100.00,
  "currency": "EUR"
}
```
---

## Kafka

#### Topic

- **orders.created.v1** (configurable via **app.kafka.topics.orderCreated**) 

#### Message Key

- **orderId** as String
#### Message Key

- JSON serialized **OrderCreatedEvent** (bytes)

example payload:
```json
{
  "eventId": "uuid",
  "orderId": "uuid",
  "amount": 100.00,
  "currency": "EUR",
  "createdAt": "2026-02-16T12:33:09.818266Z"
}
```
---
## Local run
### Prerequisites

- PostgreSQL running on **localhost:5432** with database portfolio
- Kafka running on **localhost:9092**

### Config

Default local config is in **src/main/resources/application.yml**

### Build
``` bash
mvn clean install
```
### Run
``` bash
mvn spring-boot:run
```

### Run
``` bash
curl -X POST "http://localhost:8080/api/orders" \
  -H "Content-Type: application/json" \
  -d '{"amount": 100.00, "currency":"EUR"}'
```