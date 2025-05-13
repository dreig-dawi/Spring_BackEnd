package com.example.cheffin.repository;

import com.example.cheffin.model.ChatMessage;
import com.example.cheffin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    @Query("SELECT c FROM ChatMessage c WHERE " +
           "(c.sender = :user1 AND c.recipient = :user2) OR " +
           "(c.sender = :user2 AND c.recipient = :user1) " +
           "ORDER BY c.timestamp ASC")
    List<ChatMessage> findConversation(@Param("user1") User user1, @Param("user2") User user2);
    
    @Query("SELECT DISTINCT " +
           "CASE WHEN c.sender = :user THEN c.recipient ELSE c.sender END " +
           "FROM ChatMessage c " +
           "WHERE c.sender = :user OR c.recipient = :user")
    List<User> findConversationParticipants(@Param("user") User user);
    
    @Query("SELECT c FROM ChatMessage c WHERE " +
           "c.recipient = :user AND c.read = false")
    List<ChatMessage> findUnreadMessages(@Param("user") User user);
}
