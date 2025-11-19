package com.example.notification_service.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "order.notification.queue")
    public void sendNotification(OrderCreatedEvent event) {

        // Business Logic Example
        System.out.println("✔ Sending email to user: " + event.getUserId());

        //Send Notification BroadCast
       // messagingTemplate.convertAndSend("/topic/notifications", event);

        // Send ONLY to specific user
        messagingTemplate.convertAndSendToUser(
                event.getUserId(),              // userId
                "/queue/notifications",         // frontend subscribes to /user/queue/notifications
                event
        );
    }
}
