package com.user_messaging_system.message_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.user_messaging_system.core_library.response.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import static com.user_messaging_system.core_library.common.constant.ErrorConstant.NO_ROOT_CAUSE_AVAILABLE;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ErrorResponse> messageNotFoundHandler(MessageNotFoundException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse.Builder()
                .message(exception.getMessage())
                .errors(List.of(exception.getCause() != null ? exception.getCause().getMessage() : NO_ROOT_CAUSE_AVAILABLE))
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false))
                .build()
            );
    }
}
