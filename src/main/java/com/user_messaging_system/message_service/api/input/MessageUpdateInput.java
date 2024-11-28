package com.user_messaging_system.message_service.api.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import static com.user_messaging_system.core_library.common.constant.ValidationConstant.*;

@Validated
public record MessageUpdateInput(
        @NotNull(message = MESSAGE_CONTENT_NULL)
        @Size(min = MESSAGE_CONTENT_MIN_LENGTH, max = MESSAGE_CONTENT_MAX_LENGTH, message = INVALID_MESSAGE_CONTENT)
        String content
){ }
