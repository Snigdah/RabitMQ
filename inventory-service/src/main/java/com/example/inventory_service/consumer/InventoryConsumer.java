package com.example.inventory_service.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.example.inventory_service.config.RabbitConfig.INVENTORY_QUEUE;

@Service
public class InventoryConsumer {

    @RabbitListener(queues = INVENTORY_QUEUE)
    public void handleOrderEvents(OrderCreatedEvent event) {
        System.out.println("📦 Inventory Service Received Event: " + event);

        // Business Logic (Example)
        System.out.println("✔ Decreasing stock for product: " + event.getProduct());
    }
}
