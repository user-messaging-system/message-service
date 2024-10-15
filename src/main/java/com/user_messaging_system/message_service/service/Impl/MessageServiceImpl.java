package com.user_messaging_system.message_service.service.Impl;

import com.user_messaging_system.message_service.api.input.MessageSendInput;
import com.user_messaging_system.message_service.configuration.RabbitMQConfig;
import com.user_messaging_system.message_service.dto.MessageDto;
import com.user_messaging_system.message_service.mapper.MessageMapper;
import com.user_messaging_system.message_service.model.Message;
import com.user_messaging_system.message_service.rabbitmq.producer.MessageProducer;
import com.user_messaging_system.message_service.repository.MessageRepository;
import com.user_messaging_system.message_service.service.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageProducer messageProducer;

    private final MessageRepository messageRepository;

    public MessageServiceImpl(
            MessageProducer webSocketMessageProducer,
            MessageRepository messageRepository
    ) {
        this.messageProducer = webSocketMessageProducer;
        this.messageRepository = messageRepository;
    }

    @Override
    public List<MessageDto> getMessagesBetweenUsers(String senderId, String receiverId){
        List<Message> messageList = messageRepository.getMessagesBetweenUsers(senderId, receiverId);
        return MessageMapper.INSTANCE.messageListToMessageDtoList(messageList);
    }

    @Override
    public void sendMessage(MessageSendInput messageSendInput) {
        LocalDateTime receiveTimestamp = LocalDateTime.now();
        messageSendInput.setReceiveTimestamp(receiveTimestamp);
        messageProducer.produceMessage(
                RabbitMQConfig.USER_MESSAGE_EXCHANGE,
                RabbitMQConfig.MESSAGE_WS_SEND,
                messageSendInput
        );
        messageProducer.produceMessage(
                RabbitMQConfig.USER_MESSAGE_EXCHANGE,
                RabbitMQConfig.MESSAGE_DB_SAVE,
                messageSendInput
        );
    }
}