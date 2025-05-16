package com.example.cheffin.controller;

import com.example.cheffin.dto.ChatMessageDTO;
import com.example.cheffin.dto.ConversationDTO;
import com.example.cheffin.exception.ResourceNotFoundException;
import com.example.cheffin.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDTO>> getConversations(Authentication authentication) {
        try {
            String username = authentication.getName();
            List<ConversationDTO> conversations = chatService.getConversations(username);
            return ResponseEntity.ok(conversations);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return an empty list instead of throwing a 500 error
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/messages/{participantId}")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(
            Authentication authentication,
            @PathVariable Long participantId) {
        try {
            String username = authentication.getName();
            List<ChatMessageDTO> messages = chatService.getConversation(username, participantId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return an empty list instead of throwing a 500 error
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/unread")
    public ResponseEntity<List<ChatMessageDTO>> getUnreadMessages(Authentication authentication) {
        try {
            String username = authentication.getName();
            List<ChatMessageDTO> messages = chatService.getUnreadMessages(username);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return an empty list instead of throwing a 500 error
            return ResponseEntity.ok(new ArrayList<>());
        }
    }    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            Authentication authentication,
            @RequestBody ChatMessageRequest request) {
        try {
            if (request == null || request.getRecipientUsername() == null || request.getContent() == null) {
                return ResponseEntity
                    .badRequest()
                    .body("Invalid request: recipientUsername and content are required");
            }

            String senderUsername = authentication.getName();
            ChatMessageDTO message = chatService.saveMessage(
                    senderUsername,
                    request.getRecipientUsername(),
                    request.getContent()
            );
            return ResponseEntity.ok(message);
        } catch (ResourceNotFoundException e) {
            // Handle specific case of user not found
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            // Log the exception with more details
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to send message: " + e.getMessage());
        }
    }

    // Request class for sending messages
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
