package com.user_messaging_system.message_service.repository;

import com.user_messaging_system.message_service.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    @Query("SELECT m FROM Message m WHERE (m.senderId = :senderId AND m.receiverId = :receiverId) " +
            "OR (m.senderId = :receiverId " +
            "AND m.receiverId = :senderId) ORDER BY m.receiveTime")
    List<Message> getMessagesBetweenUsers(String senderId, String receiverId);
}
