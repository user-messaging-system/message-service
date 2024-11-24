package com.user_messaging_system.message_service.common.constant;

public class ApiConstant {
    private ApiConstant() {
        throw new AssertionError("This class should not be instantiated");
    }

    public static final String API_VERSION = "/v1/api/messages";

    public static final String MESSAGE_SUCCESSFULLY_SENT_MESSAGE = "Message sent successfully";
    public static final String MESSAGE_SUCCESSFULLY_RETRIEVED_MESSAGE = "Messages retrieved successfully";
    public static final String MESSAGE_SUCCESSFULLY_UPDATED_MESSAGE = "Message updated successfully";
    public static final String MESSAGE_SUCCESSFULLY_DELETED_MESSAGE = "Message deleted successfully";
}
