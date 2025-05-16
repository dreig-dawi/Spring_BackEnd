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
    }    public List<ConversationDTO> getConversations(String username) {
        List<ConversationDTO> conversations = new ArrayList<>();
        
        try {
            // Find the current user
            User currentUser = userService.findByUsername(username);
            // User should never be null as findByUsername throws an exception if not found
            System.out.println("Found user: " + currentUser.getUsername() + " (ID: " + currentUser.getId() + ")");
            
            // Find participants
            List<User> participants;            
            try {
                // Get all messages first to check if there are any
                List<ChatMessage> allMessages = chatMessageRepository.findAll();
                System.out.println("Total messages in database: " + allMessages.size());
                if (!allMessages.isEmpty()) {
                    // Print some examples for debugging
                    for (int i = 0; i < Math.min(3, allMessages.size()); i++) {
                        ChatMessage msg = allMessages.get(i);
                        System.out.println(String.format("Message %d: From %s to %s: %s",
                            msg.getId(),
                            msg.getSender().getUsername(),
                            msg.getRecipient().getUsername(),
                            msg.getContent().substring(0, Math.min(20, msg.getContent().length())) + "..."));
                    }
                }
                  // Use native SQL query directly instead of the problematic JPQL query
                // This is more reliable and avoids the ClassCastException
                System.out.println("Using native SQL query to find conversation participants");
                List<Long> participantIds = chatMessageRepository.findConversationParticipantIdsByUserId(currentUser.getId());
                
                participants = new ArrayList<>();
                if (participantIds != null && !participantIds.isEmpty()) {
                    System.out.println("Native query found " + participantIds.size() + " participant IDs");
                    for (Long id : participantIds) {
                        try {
                            User participant = userService.findById(id);
                            if (participant != null) {
                                participants.add(participant);
                            }
                        } catch (Exception e) {
                            System.err.println("Could not find user with ID " + id + ": " + e.getMessage());
                        }
                    }
                    System.out.println("Successfully loaded " + participants.size() + " participants from IDs");
                } else {
                    System.out.println("No conversation participants found using native query");
                }
            } catch (Exception e) {
                // If there's any error with the query, log it and return an empty list
                System.err.println("Error finding conversation participants: " + e.getMessage());
                e.printStackTrace();
                return new ArrayList<>();
            }
            
            // Initialize conversations list
              // Process only if we have participants
            if (participants != null && !participants.isEmpty()) {
                System.out.println("Processing " + participants.size() + " participants to create conversation DTOs");
                for (User participant : participants) {
                    // Skip null participants
                    if (participant == null) {
                        System.out.println("Skipping null participant");
                        continue;
                    }
                    
                    try {
                        System.out.println("Processing conversation with: " + participant.getUsername() + " (ID: " + participant.getId() + ")");
                        // Find conversation messages
                        List<ChatMessage> messages = chatMessageRepository.findConversation(currentUser, participant);
                        
                        // Only process if we have messages
                        if (messages != null && !messages.isEmpty()) {
                            System.out.println("Found " + messages.size() + " messages with " + participant.getUsername());
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
                            System.out.println("Added conversation with: " + participant.getUsername());
                        } else {
                            System.out.println("No messages found with " + participant.getUsername() + ", skipping conversation");
                        }
                    } catch (Exception e) {
                        // If we have an issue with one conversation, log it and skip it but don't fail the entire request
                        System.err.println("Error processing conversation with " + participant.getUsername() + ": " + e.getMessage());
                        continue;
                    }                }
            } else {
                System.out.println("No participants found for user: " + currentUser.getUsername());
            }
            
            System.out.println("Returning " + conversations.size() + " conversations");
        } catch (Exception e) {
            // Catch any unexpected errors and return an empty list instead of throwing 500
            System.err.println("Unexpected error in getConversations: " + e.getMessage());
            e.printStackTrace();
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
