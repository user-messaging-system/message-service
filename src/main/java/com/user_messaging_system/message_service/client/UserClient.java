package com.user_messaging_system.message_service.client;

import com.user_messaging_system.core_library.response.SuccessResponse;
import com.user_messaging_system.message_service.api.output.UserGetOutput;
import com.user_messaging_system.message_service.configuration.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserClient {
    @GetMapping("/v1/api/users/{id}")
    ResponseEntity<SuccessResponse<UserGetOutput>> getById(@PathVariable(name = "id") String id);
}
