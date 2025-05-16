package com.example.cheffin.service;

import com.example.cheffin.dto.ChatMessageDTO;
import com.example.cheffin.dto.ConversationDTO;
import com.example.cheffin.exception.ResourceNotFoundException;
import com.example.cheffin.model.ChatMessage;
import com.example.cheffin.model.User;
import com.example.cheffin.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;    public List<ChatMessageDTO> getConversation(String username, Long participantId) {
        try {
            User currentUser = userService.findByUsername(username);
            if (currentUser == null) {
                return new ArrayList<>();
            }
            
            User participant;
            try {
                participant = userService.findById(participantId);
            } catch (Exception e) {
                return new ArrayList<>();
            }
            
            if (participant == null) {
                return new ArrayList<>();
            }

            List<ChatMessage> messages;
            try {
                messages = chatMessageRepository.findConversation(currentUser, participant);
            } catch (Exception e) {
                return new ArrayList<>();
            }
            
            // Mark messages as read if they were sent to the current user
            if (messages != null && !messages.isEmpty()) {
                messages.forEach(msg -> {
                    if (msg.getRecipient().getId().equals(currentUser.getId()) && !msg.isRead()) {
                        msg.setRead(true);
                        chatMessageRepository.save(msg);
                    }
                });
            } else {
                // Return empty list if no messages found
                return new ArrayList<>();
            }

            return messages.stream()
                    .map(ChatMessageDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Return empty list for any unexpected error
            return new ArrayList<>();
        }
    }

    public List<ConversationDTO> getConversations(String username) {
        List<ConversationDTO> conversations = new ArrayList<>();
        
        try {
            // Find the current user
            User currentUser = userService.findByUsername(username);
            if (currentUser == null) {
                return new ArrayList<>(); // Return empty list if user not found
            }
            
            // Find participants
            List<User> participants;
            try {
                participants = chatMessageRepository.findConversationParticipants(currentUser);
            } catch (Exception e) {
                // If there's any error with the query, just return an empty list
                return new ArrayList<>();
            }
            
            // Initialize conversations list
            
            // Process only if we have participants
            if (participants != null && !participants.isEmpty()) {
                for (User participant : participants) {
                    // Skip null participants
                    if (participant == null) {
                        continue;
                    }
                    
                    try {
                        // Find conversation messages
                        List<ChatMessage> messages = chatMessageRepository.findConversation(currentUser, participant);
                        
                        // Only process if we have messages
                        if (messages != null && !messages.isEmpty()) {
                            // Get the most recent message safely
                            ChatMessage lastMessage = messages.get(messages.size() - 1);
                            
                            // Create conversation DTO with null checks
                            ConversationDTO conversation = ConversationDTO.builder()
                                    .participantId(participant.getId())
                                    .username(participant.getUsername())
                                    .lastMessage(lastMessage.getContent() != null ? lastMessage.getContent() : "")
                                    .timestamp(lastMessage.getTimestamp() != null ? lastMessage.getTimestamp() : LocalDateTime.now())
                                    .build();
                            
                            conversations.add(conversation);
                        }
                    } catch (Exception e) {
                        // If we have an issue with one conversation, skip it but don't fail the entire request
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            // Catch any unexpected errors and return an empty list instead of throwing 500
            return new ArrayList<>();
        }
        
        return conversations;
    }    public ChatMessageDTO saveMessage(String senderUsername, String recipientUsername, String content) {
        try {
            // Add debug logging
            System.out.println("Attempting to save message from: " + senderUsername + " to: " + recipientUsername);
            
            if (senderUsername == null || senderUsername.trim().isEmpty()) {
                throw new IllegalArgumentException("Sender username cannot be empty");
            }
            
            if (recipientUsername == null || recipientUsername.trim().isEmpty()) {
                throw new IllegalArgumentException("Recipient username cannot be empty");
            }
            
            User sender = userService.findByUsername(senderUsername);
            if (sender == null) {
                throw new ResourceNotFoundException("Sender not found: " + senderUsername);
            }
            
            User recipient = userService.findByUsername(recipientUsername);
            if (recipient == null) {
                throw new ResourceNotFoundException("Recipient not found: " + recipientUsername);
            }
            
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("Message content cannot be empty");
            }

            System.out.println("Building chat message...");
            ChatMessage message = ChatMessage.builder()
                    .sender(sender)
                    .recipient(recipient)
                    .content(content)
                    .timestamp(LocalDateTime.now())
                    .read(false)
                    .build();

            System.out.println("Saving message to database...");
            ChatMessage savedMessage = chatMessageRepository.save(message);
            
            if (savedMessage == null || savedMessage.getId() == null) {
                throw new RuntimeException("Failed to save message");
            }
            
            System.out.println("Message saved successfully with ID: " + savedMessage.getId());
            return ChatMessageDTO.fromEntity(savedMessage);
        } catch (Exception e) {
            System.err.println("Error in saveMessage: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to be handled by controller or WebSocket error handler
        }
    }public List<ChatMessageDTO> getUnreadMessages(String username) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return new ArrayList<>();
            }
            
            List<ChatMessage> unreadMessages;
            try {
                unreadMessages = chatMessageRepository.findUnreadMessages(user);
                if (unreadMessages == null) {
                    return new ArrayList<>();
                }
            } catch (Exception e) {
                return new ArrayList<>();
            }
            
            return unreadMessages.stream()
                    .map(ChatMessageDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
