package com.order.controller;

import com.order.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // 클라이언트에서 메시지를 수신하고, 해당 메시지를 /sub/chat 경로로 발행
    @MessageMapping("/chat") // 클라이언트가 /pub/chat에 메시지를 발행하면 이 메소드가 실행
    @SendTo("/sub/chat") // 해당 메시지를 /sub/chat 경로를 구독하는 모든 클라이언트에게 전송
    public ChatMessage sendMessage(ChatMessage message) throws Exception {
        return message; // 받은 메시지를 그대로 전송
    }
}