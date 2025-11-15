package com.example.order_service.service;

import com.example.order_service.config.RabbitConfig;
import com.example.order_service.dto.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final RabbitTemplate rabbitTemplate;

    public OrderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrder(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EXCHANGE,  // exchange name
                RabbitConfig.ORDER_CREATED,  // routing key
                event
        );

        System.out.println("📤 Sent Order Event: " + event);
    }
}
