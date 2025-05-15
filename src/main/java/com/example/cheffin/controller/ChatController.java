package com.example.cheffin.controller;

import com.example.cheffin.dto.ChatMessageDTO;
import com.example.cheffin.dto.ConversationDTO;
import com.example.cheffin.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;    @GetMapping("/conversations")
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
    }    @GetMapping("/messages/{participantId}")
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
    }
}
