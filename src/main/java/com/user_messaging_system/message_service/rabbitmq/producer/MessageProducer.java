package com.user_messaging_system.message_service.rabbitmq.producer;

import com.user_messaging_system.message_service.api.input.MessageSendInput;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void produceMessage(String exchange, String routingKey, MessageSendInput messageSendInput) {
        rabbitTemplate.convertAndSend(exchange, routingKey, messageSendInput);
    }
}
