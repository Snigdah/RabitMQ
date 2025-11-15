# RabbitMQ Event-Driven Microservices Architecture

## Overview

This project demonstrates a complete event-driven microservices architecture using RabbitMQ Topic Exchange pattern. It showcases industry-standard practices for building loosely coupled, scalable microservices.

## Architecture Diagram

<div align="center">
  <img src="https://raw.githubusercontent.com/Snigdah/images/refs/heads/main/rabbitmq_bw_diagram.svg" alt="RabbitMQ Microservices Architecture" width="100%">
</div>

---

## Key Components

### 🔷 Producer (Order Service)
- **Responsibility**: Creates the `order.exchange` and publishes events
- **Routing Keys**: 
  - `order.created` - New order placed
  - `order.cancelled` - Order cancelled
  - `order.shipped` - Order shipped
- **Pattern**: Publishes only, does NOT create queues (loose coupling)

### 🔶 Topic Exchange (order.exchange)
- **Type**: TOPIC
- **Function**: Routes messages based on wildcard pattern matching
- **Patterns**:
  - `*` = matches exactly one word
  - `#` = matches zero or more words

### 🟢 Consumer 1: Inventory Service
- **Queue**: `order.inventory.queue`
- **Binding Key**: `order.*`
- **Receives**: ALL order events (created, cancelled, shipped)
- **Purpose**: Updates stock levels for any order event
- **Dead Letter**: Configured to `order.deadletter.exchange`

### 🟠 Consumer 2: Notification Service
- **Queue**: `order.notification.queue`
- **Binding Key**: `order.created`
- **Receives**: ONLY new order events
- **Purpose**: Sends email/SMS notifications for new orders only
- **Dead Letter**: Configured to `order.deadletter.exchange`

### 🔴 Dead Letter Queue (DLX)
- **Exchange**: `order.deadletter.exchange`
- **Queue**: `order.deadletter.queue`
- **Binding**: `#` (catch-all pattern)
- **Purpose**: Centralized failure handling, retry logic, and monitoring

---

## Routing Logic

| Routing Key | Inventory Service | Notification Service | Reason |
|-------------|-------------------|----------------------|--------|
| `order.created` | ✓ YES | ✓ YES | Matches both `order.*` and `order.created` |
| `order.cancelled` | ✓ YES | ✗ NO | Matches `order.*` only |
| `order.shipped` | ✓ YES | ✗ NO | Matches `order.*` only |

---

## Industry Best Practices

✅ **Loose Coupling**: Producer doesn't know about consumers  
✅ **Consumer Autonomy**: Each consumer creates its own queue and binding  
✅ **Scalability**: New consumers can subscribe without modifying producer  
✅ **Failure Handling**: Dead Letter Queue for centralized error management  
✅ **Topic Exchange**: Flexible routing based on business logic  

---

## Docker Setup

### Docker Compose Configuration
```yaml
services:
  rabbitmq:
    image: rabbitmq:3.12-management
    container_name: rabbitmq
    restart: unless-stopped
    ports:
      - "5672:5672"     # RabbitMQ (AMQP) port
      - "15672:15672"   # Management UI port
    environment:
      RABBITMQ_DEFAULT_USER: admin
      RABBITMQ_DEFAULT_PASS: admin
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq

volumes:
  rabbitmq_data:
```

### Quick Start

1. **Start RabbitMQ**:
```bash
   docker-compose up -d
```

2. **Access Management UI**:
   - URL: http://localhost:15672
   - Username: `admin`
   - Password: `admin
