package com.user_messaging_system.message_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String USER_MESSAGE_EXCHANGE = "user-message-exchange";
    public static final String USER_MESSAGE_QUEUE = "user-message-queue";
    public static final String USER_MESSAGE_ROUTING_KEY = "user-message-routing-key";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(USER_MESSAGE_EXCHANGE, false, true);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(USER_MESSAGE_QUEUE)
                .exclusive()
                .autoDelete()
                .build();
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(USER_MESSAGE_ROUTING_KEY);
    }
}