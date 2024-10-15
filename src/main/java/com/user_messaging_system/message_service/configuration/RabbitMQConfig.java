package com.user_messaging_system.message_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String USER_MESSAGE_EXCHANGE = "user-message-exchange";
    public static final String MESSAGE_PERSISTENCE_QUEUE = "message-persistence-queue";
    public static final String MESSAGE_WS_SEND_QUEUE = "message-ws-send-queue";
    public static final String MESSAGE_DB_SAVE = "message-db-save";
    public static final String MESSAGE_WS_SEND = "message-ws-send";
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(USER_MESSAGE_EXCHANGE, false, true);
    }

    @Bean
    public Queue wsQueue() {
        return QueueBuilder.durable(MESSAGE_WS_SEND_QUEUE)
                .exclusive()
                .autoDelete()
                .build();
    }

    @Bean
    public Queue dbQueue() {
        return QueueBuilder.durable(MESSAGE_PERSISTENCE_QUEUE)
                .exclusive()
                .autoDelete()
                .build();
    }

    @Bean
    public Binding wsBinding(Queue wsQueue, DirectExchange exchange) {
        return BindingBuilder.bind(wsQueue())
                .to(exchange())
                .with(MESSAGE_WS_SEND);
    }

    @Bean
    public Binding dbBinding(Queue dbQueue, DirectExchange exchange) {
        return BindingBuilder.bind(dbQueue())
                .to(exchange())
                .with(MESSAGE_DB_SAVE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory myFactory(ConnectionFactory connectionFactory, MessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        return factory;
    }

}