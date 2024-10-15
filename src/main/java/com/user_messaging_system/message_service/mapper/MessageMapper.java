package com.user_messaging_system.message_service.mapper;

import com.user_messaging_system.message_service.api.output.MessageGetOutput;
import com.user_messaging_system.message_service.dto.MessageDto;
import com.user_messaging_system.message_service.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageDto messageToMessageDto(Message message);
    List<MessageDto> messageListToMessageDtoList(List<Message> messageList);

    List<MessageGetOutput> messageDtoListToMessageGetOutputList(List<MessageDto> messageDtoListe);
}
