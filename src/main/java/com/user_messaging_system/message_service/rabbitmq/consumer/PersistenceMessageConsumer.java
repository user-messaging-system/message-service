package com.user_messaging_system.message_service.rabbitmq.consumer;

import com.user_messaging_system.message_service.api.input.MessageSendInput;
import com.user_messaging_system.message_service.model.Message;
import com.user_messaging_system.message_service.repository.MessageRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PersistenceMessageConsumer {
    private final MessageRepository messageRepository;

    public PersistenceMessageConsumer(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @RabbitListener(queues = "message-persistence-queue")
    public void receiveMessage(MessageSendInput messageSendInput){
        //System.out.println("Received message from RabbitMQ PersistenceMessageConsumer: " + messageSendInput.getContent());
        Message message = new Message();
        message.setSenderId(messageSendInput.getSenderId());
        message.setReceiverId(messageSendInput.getReceiverId());
        message.setContent(messageSendInput.getContent());
        message.setReceiveTime(messageSendInput.getReceiveTimestamp());
        this.messageRepository.save(message);
    }
}
