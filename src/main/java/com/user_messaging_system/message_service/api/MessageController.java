package com.user_messaging_system.message_service.api;

import com.user_messaging_system.core_library.common.constant.ValidationConstant;
import com.user_messaging_system.core_library.response.SuccessResponse;
import com.user_messaging_system.message_service.api.input.MessageSendInput;
import com.user_messaging_system.message_service.api.input.MessageUpdateInput;
import com.user_messaging_system.message_service.api.output.MessageGetOutput;
import com.user_messaging_system.message_service.api.output.MessageUpdateOutput;
import com.user_messaging_system.message_service.mapper.MessageMapper;
import com.user_messaging_system.message_service.service.MessageService;
import static com.user_messaging_system.core_library.common.constant.APIConstant.*;
import static com.user_messaging_system.core_library.common.constant.MessageConstant.*;
import static com.user_messaging_system.core_library.common.constant.ValidationConstant.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(MESSAGE_SERVICE_BASE_URL)
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping(PAIR_PATH)
    public ResponseEntity<SuccessResponse<List<MessageGetOutput>>> getMessagesBetweenUsers(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam @Valid @Size(min = UUID_LENGTH, max = UUID_LENGTH, message = INVALID_SENDER_ID) String senderId,
            @RequestParam @Valid @Size(min = UUID_LENGTH, max = UUID_LENGTH, message = INVALID_RECEIVER_ID) String receiverId
    ){
        SuccessResponse<List<MessageGetOutput>> response = new SuccessResponse.Builder<List<MessageGetOutput>>()
            .message(MESSAGES_SUCCESSFULLY_RETRIEVED)
            .data(MessageMapper.INSTANCE.messageDtoListToMessageGetOutputList(
                    messageService.getMessagesBetweenUsers(jwtToken, senderId, receiverId))
            )
            .status(HttpStatus.OK.toString())
            .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public void sendMessage(@RequestBody MessageSendInput messageSendInput){
        messageService.sendMessage(messageSendInput);
    }

    @PutMapping("{messageId}")
    public ResponseEntity<SuccessResponse<MessageUpdateOutput>> updateMessage(
            @Valid
            @Size(min = UUID_LENGTH, max = UUID_LENGTH, message = ValidationConstant.INVALID_USER_ID)
            @PathVariable String messageId,
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody MessageUpdateInput messageUpdateInput
    ){
        SuccessResponse<MessageUpdateOutput> response = new SuccessResponse.Builder<MessageUpdateOutput>()
            .message(MESSAGE_SUCCESSFULLY_UPDATED)
            .data(MessageMapper.INSTANCE.messageDtoToMessageUpdateOutput(
                    messageService.updateMessageById(messageId, jwtToken, messageUpdateInput)
            ))
            .status(HttpStatus.OK.toString())
            .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{messageId}")
    public void deleteById(
            @RequestHeader("Authorization") String jwtToken,
            @Valid
            @Size(min = UUID_LENGTH, max = UUID_LENGTH, message = ValidationConstant.INVALID_MESSAGE_ID)
            @PathVariable(name = "messageId") String messageId
    ){
        messageService.deleteById(messageId, jwtToken);
    }
}