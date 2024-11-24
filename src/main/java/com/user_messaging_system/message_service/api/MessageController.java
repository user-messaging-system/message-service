package com.user_messaging_system.message_service.api;

import com.user_messaging_system.core_library.response.SuccessResponse;
import com.user_messaging_system.message_service.api.input.MessageSendInput;
import com.user_messaging_system.message_service.api.input.MessageUpdateInput;
import com.user_messaging_system.message_service.api.output.MessageGetOutput;
import com.user_messaging_system.message_service.api.output.MessageUpdateOutput;
import com.user_messaging_system.message_service.mapper.MessageMapper;
import com.user_messaging_system.message_service.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.user_messaging_system.message_service.common.constant.ApiConstant.*;

@RestController
@RequestMapping(API_VERSION)
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping("/conversations/{senderId}/{receiverId}")
    public ResponseEntity<SuccessResponse<List<MessageGetOutput>>> getMessagesBetweenUsers(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable String senderId,
            @PathVariable String receiverId
    ){
        SuccessResponse<List<MessageGetOutput>> response = new SuccessResponse.Builder<List<MessageGetOutput>>()
            .message(MESSAGE_SUCCESSFULLY_RETRIEVED_MESSAGE)
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
            @PathVariable String messageId,
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody MessageUpdateInput messageUpdateInput
    ){
        SuccessResponse<MessageUpdateOutput> response = new SuccessResponse.Builder<MessageUpdateOutput>()
            .message(MESSAGE_SUCCESSFULLY_UPDATED_MESSAGE)
            .data(
                    MessageMapper.INSTANCE.messageDtoToMessageUpdateOutput(
                        messageService.updateMessageById(messageId, jwtToken, messageUpdateInput)
                    )
            )
            .status(HttpStatus.OK.toString())
            .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable(name = "messageId") String messageId
    ){
        return null;
    }
}