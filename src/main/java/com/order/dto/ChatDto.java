package com.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private Long id;
    private Long chatId;
    private String sender;
    private String content;
    private String date;
    private Boolean isUser;
}
