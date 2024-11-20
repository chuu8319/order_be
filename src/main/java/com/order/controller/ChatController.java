package com.order.controller;

import com.order.common.AuthUser;
import com.order.entity.ChatMessage;
import com.order.entity.User;
import com.order.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;
    @PostMapping("/start/{id}")
    public ResponseEntity<?> startChat(@AuthUser User user, @PathVariable(value = "id") Long id) {
        Long chatId = chatService.startChat(user, id);
        return ResponseEntity.ok(chatId);
    }

    @PostMapping("/chat/{id}")
    public ResponseEntity<?> messageChat(@AuthUser User user, @PathVariable(value = "id")Long id, @RequestBody Map<String, String> request) {
        String content = request.get("content");
        Long chatId = chatService.messageChat(user, id, content);
        return ResponseEntity.ok(chatId);
    }

    @GetMapping("/chat/{id}")
    public ResponseEntity<?> chat(@AuthUser User user, @PathVariable(value = "id") Long id) {
        List<ChatMessage> chatMessages = chatService.chat(user, id);
        return ResponseEntity.ok(chatMessages);
    }
}