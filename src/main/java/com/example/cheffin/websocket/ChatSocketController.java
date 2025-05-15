package com.example.cheffin.websocket;

import com.example.cheffin.dto.ChatMessageDTO;
import com.example.cheffin.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/send_message")
    public void processMessage(@Payload ChatMessageRequest chatMessageRequest, Principal principal) {
        try {
            String senderUsername = principal.getName();
            
            // Validate request
            if (chatMessageRequest == null || 
                chatMessageRequest.getRecipientUsername() == null || 
                chatMessageRequest.getContent() == null) {
                sendErrorToUser(senderUsername, "Invalid message format");
                return;
            }
            
            // Save the message to database
            ChatMessageDTO message = chatService.saveMessage(
                    senderUsername, 
                    chatMessageRequest.getRecipientUsername(),
                    chatMessageRequest.getContent()
            );
            
            // Send the message to the recipient
            messagingTemplate.convertAndSendToUser(
                    chatMessageRequest.getRecipientUsername(),
                    "/queue/messages",
                    message
            );
            
            // Send a copy to the sender as well
            messagingTemplate.convertAndSendToUser(
                    senderUsername,
                    "/queue/messages",
                    message
            );
        } catch (Exception e) {
            e.printStackTrace();
            // Send error message to sender
            if (principal != null) {
                sendErrorToUser(principal.getName(), "Error processing message: " + e.getMessage());
            }
        }
    }
    
    private void sendErrorToUser(String username, String errorMessage) {
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/errors",
                errorMessage
        );
    }
    
    public static class ChatMessageRequest {
        private String recipientUsername;
        private String content;
        
        public String getRecipientUsername() {
            return recipientUsername;
        }
        
        public void setRecipientUsername(String recipientUsername) {
            this.recipientUsername = recipientUsername;
        }
        
        public String getContent() {
            return content;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
    }
}
