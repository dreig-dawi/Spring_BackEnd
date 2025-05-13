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
    private final UserService userService;

    public List<ChatMessageDTO> getConversation(String username, Long participantId) {
        User currentUser = userService.findByUsername(username);
        User participant = userService.findById(participantId);

        List<ChatMessage> messages = chatMessageRepository.findConversation(currentUser, participant);
        
        // Mark messages as read if they were sent to the current user
        messages.forEach(msg -> {
            if (msg.getRecipient().getId().equals(currentUser.getId()) && !msg.isRead()) {
                msg.setRead(true);
                chatMessageRepository.save(msg);
            }
        });

        return messages.stream()
                .map(ChatMessageDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ConversationDTO> getConversations(String username) {
        User currentUser = userService.findByUsername(username);
        List<User> participants = chatMessageRepository.findConversationParticipants(currentUser);
        
        List<ConversationDTO> conversations = new ArrayList<>();
        
        for (User participant : participants) {
            List<ChatMessage> messages = chatMessageRepository.findConversation(currentUser, participant);
            
            if (!messages.isEmpty()) {
                // Get the most recent message
                ChatMessage lastMessage = messages.get(messages.size() - 1);
                
                ConversationDTO conversation = ConversationDTO.builder()
                        .participantId(participant.getId())
                        .username(participant.getUsername())
                        .lastMessage(lastMessage.getContent())
                        .timestamp(lastMessage.getTimestamp())
                        .build();
                
                conversations.add(conversation);
            }
        }
        
        return conversations;
    }

    public ChatMessageDTO saveMessage(String senderUsername, String recipientUsername, String content) {
        User sender = userService.findByUsername(senderUsername);
        User recipient = userService.findByUsername(recipientUsername);

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .content(content)
                .timestamp(LocalDateTime.now())
                .read(false)
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(message);
        return ChatMessageDTO.fromEntity(savedMessage);
    }

    public List<ChatMessageDTO> getUnreadMessages(String username) {
        User user = userService.findByUsername(username);
        return chatMessageRepository.findUnreadMessages(user).stream()
                .map(ChatMessageDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
