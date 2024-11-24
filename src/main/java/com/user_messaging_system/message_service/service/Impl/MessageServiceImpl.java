package com.user_messaging_system.message_service.service.Impl;

import com.user_messaging_system.core_library.exception.UnauthorizedException;
import com.user_messaging_system.core_library.exception.UserNotFoundException;
import com.user_messaging_system.core_library.response.SuccessResponse;
import com.user_messaging_system.core_library.service.JWTService;
import com.user_messaging_system.message_service.api.input.MessageSendInput;
import com.user_messaging_system.message_service.api.input.MessageUpdateInput;
import com.user_messaging_system.message_service.api.output.UserGetOutput;
import com.user_messaging_system.message_service.client.UserClient;
import com.user_messaging_system.message_service.configuration.RabbitMQConfig;
import com.user_messaging_system.message_service.dto.MessageDto;
import com.user_messaging_system.message_service.exception.MessageNotFoundException;
import com.user_messaging_system.message_service.mapper.MessageMapper;
import com.user_messaging_system.message_service.model.Message;
import com.user_messaging_system.message_service.rabbitmq.producer.MessageProducer;
import com.user_messaging_system.message_service.repository.MessageRepository;
import com.user_messaging_system.message_service.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageProducer messageProducer;
    private final UserClient userClient;
    private final MessageRepository messageRepository;
    private final JWTService jwtService;

    public MessageServiceImpl(
            MessageProducer webSocketMessageProducer,
            MessageRepository messageRepository,
            JWTService jwtService,
            UserClient userClient
    ) {
        this.messageProducer = webSocketMessageProducer;
        this.messageRepository = messageRepository;
        this.jwtService = jwtService;
        this.userClient = userClient;
    }

    @Override
    public List<MessageDto> getMessagesBetweenUsers(String jwtToken, String senderId, String receiverId){
        String id = validateTokenAndExtractUserId(jwtToken);
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

    @Override
    public MessageDto updateMessageById(String messageId, String jwtToken, MessageUpdateInput messageUpdateInput){
        String token = jwtService.extractToken(jwtToken);
        jwtService.validateToken(token);
        String userId = jwtService.extractUserId(token);
        ResponseEntity<SuccessResponse<UserGetOutput>> userGetOutputResponseEntity = userClient.getById(userId);
        UserGetOutput userGetOutput = valiteUserGetOutput(userGetOutputResponseEntity);
        validateUserAuthorizedUpdateMessageById(userId, userGetOutput);
        Message message = findMessageById(messageId);
        message.setContent(messageUpdateInput.content());
        return MessageMapper.INSTANCE.messageToMessageDto(messageRepository.save(message));
    }

    private Message findMessageById(String id){
        return messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException("Message Not Found"));
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

    private String validateTokenAndExtractUserId(String jwtToken){
        return jwtService.extractUserId(jwtToken.substring(7));
    }

    private UserGetOutput valiteUserGetOutput(ResponseEntity<SuccessResponse<UserGetOutput>> userGetOutputResponseEntity){
        return Optional.ofNullable(userGetOutputResponseEntity)
                .map(ResponseEntity::getBody)
                .map(SuccessResponse::getData)
                .orElseThrow(() -> new UserNotFoundException("User not found or data is null"));
    }

    private void validateUserAuthorizedUpdateMessageById(String senderId, UserGetOutput userGetOutput){
        if(!senderId.equals(userGetOutput.id())){
            throw new UnauthorizedException("User is not authorized to update this message");
        }
    }
}