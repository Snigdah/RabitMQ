package com.example.notification_service.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @RabbitListener(queues = "order.notification.queue")
    public void sendNotification(OrderCreatedEvent event) {
        System.out.println("📧 Notification Service Received Event: " + event);

        // Business Logic Example
        System.out.println("✔ Sending email to user: " + event.getUserId());
    }
}
