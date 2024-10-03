package com.user_messaging_system.message_service.service.Impl;

import com.user_messaging_system.message_service.api.input.MessageSendInput;
import com.user_messaging_system.message_service.configuration.RabbitMQConfig;
import com.user_messaging_system.message_service.service.MessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfig rabbitMQConfig;

    public MessageServiceImpl(RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    @Override
    public void sendMessage(MessageSendInput messageSendInput) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_MESSAGE_EXCHANGE,
                RabbitMQConfig.USER_MESSAGE_ROUTING_KEY,
                messageSendInput
        );
    }
}