## RabbitMQ Architecture

<div align="center">
  <img src="https://raw.githubusercontent.com/Snigdah/images/refs/heads/main/rabbitmq_bw_diagram.svg" alt="RabbitMQ Microservices Architecture" width="100%">
</div>

This diagram illustrates the complete event-driven architecture using RabbitMQ Topic Exchange pattern.

### Architecture Overview:
- **Producer (Order Service)**: Publishes order events with routing keys
- **Topic Exchange (order.exchange)**: Routes messages using wildcard pattern matching
- **Inventory Service**: Receives ALL order events (binding: `order.*`)
- **Notification Service**: Receives ONLY new order events (binding: `order.created`)
- **Dead Letter Queue**: Centralized failure handling for all consumers
