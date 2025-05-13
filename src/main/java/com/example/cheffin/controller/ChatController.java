package com.example.cheffin.controller;

import com.example.cheffin.dto.ChatMessageDTO;
import com.example.cheffin.dto.ConversationDTO;
import com.example.cheffin.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDTO>> getConversations(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(chatService.getConversations(username));
    }

    @GetMapping("/messages/{participantId}")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(
            Authentication authentication,
            @PathVariable Long participantId) {
        String username = authentication.getName();
        return ResponseEntity.ok(chatService.getConversation(username, participantId));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<ChatMessageDTO>> getUnreadMessages(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(chatService.getUnreadMessages(username));
    }
}
