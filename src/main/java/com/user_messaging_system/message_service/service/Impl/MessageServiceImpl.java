package com.user_messaging_system.message_service.service.Impl;

import com.user_messaging_system.core_library.exception.UserNotFoundException;
import com.user_messaging_system.core_library.service.JWTService;
import com.user_messaging_system.message_service.api.input.MessageSendInput;
import com.user_messaging_system.message_service.configuration.RabbitMQConfig;
import com.user_messaging_system.message_service.dto.MessageDto;
import com.user_messaging_system.message_service.mapper.MessageMapper;
import com.user_messaging_system.message_service.model.Message;
import com.user_messaging_system.message_service.rabbitmq.producer.MessageProducer;
import com.user_messaging_system.message_service.repository.MessageRepository;
import com.user_messaging_system.message_service.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageProducer messageProducer;
    private final MessageRepository messageRepository;
    private final JWTService jwtService;

    public MessageServiceImpl(
            MessageProducer webSocketMessageProducer,
            MessageRepository messageRepository,
            JWTService jwtService
    ) {
        this.messageProducer = webSocketMessageProducer;
        this.messageRepository = messageRepository;
        this.jwtService = jwtService;
    }

    @Override
    public List<MessageDto> getMessagesBetweenUsers(String jwtToken, String senderId, String receiverId){
        String id = validateTokenAndExctractUserId(jwtToken);
        validateUserAccessToConversation(id, senderId, receiverId);
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

    @Override
    public void deleteById(String jwtToken, String id){
        String userId = jwtService.extractUserId(jwtToken);
        Message message = findMessageById(id);
        validateUserIsSender(userId, message.getSenderId());
        messageRepository.delete(message);
    }

    private Message findMessageById(String id){
        return messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message Not Found"));
    }

    private void validateUserAccessToConversation(String currentUserId, String senderId, String receiverId){
        if(currentUserId.equals(senderId) || currentUserId.equals(receiverId)){
            return;
        }
        throw new UserNotFoundException("Current user is not authorized to access this conversation.");
    }

    //TODO: Mesaji silme yetkisi olmadigi zaman hata mesajini duzenle
    private void validateUserIsSender(String userId, String senderId){
        if(userId.equals(senderId)){
            return;
        }
        throw new UserNotFoundException("Error delete message");
    }

    private String validateTokenAndExctractUserId(String jwtToken){
        return jwtService.extractUserId(jwtToken.substring(7));
    }
}