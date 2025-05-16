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
    List<ChatMessage> findConversation(@Param("user1") User user1, @Param("user2") User user2);    // Native SQL query (more reliable than JPQL for CASE expressions)
    @Query(value = "SELECT DISTINCT " +
           "CASE WHEN cm.sender_id = :#{#user.id} THEN cm.recipient_id ELSE cm.sender_id END AS user_id " +
           "FROM chat_messages cm " +
           "WHERE cm.sender_id = :#{#user.id} OR cm.recipient_id = :#{#user.id}", 
           nativeQuery = true)
    List<Long> findConversationParticipantIds(@Param("user") User user);
      @Query("SELECT c FROM ChatMessage c WHERE " +
           "c.recipient = :user AND c.read = false")
    List<ChatMessage> findUnreadMessages(@Param("user") User user);    
    
    @Query(value = "SELECT DISTINCT " +
           "CASE WHEN cm.sender_id = :userId THEN cm.recipient_id ELSE cm.sender_id END AS participant_id " +
           "FROM chat_messages cm " +
           "WHERE cm.sender_id = :userId OR cm.recipient_id = :userId", 
           nativeQuery = true)
    List<Long> findConversationParticipantIdsByUserId(@Param("userId") Long userId);
}
