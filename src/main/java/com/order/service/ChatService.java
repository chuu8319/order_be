package com.order.service;

import com.order.dto.ChatDto;
import com.order.entity.Chat;
import com.order.entity.ChatMessage;
import com.order.entity.Restaurant;
import com.order.entity.User;
import com.order.exception.ResourceNotFoundException;
import com.order.repository.ChatMessageRepository;
import com.order.repository.ChatRepository;
import com.order.repository.RestaurantRepository;
import com.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ChatMessageRepository chatMessageRepository;

    public Long startChat(User user, Long id) {
        User customer = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("가게가 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        User owner = restaurant.getUser();

        //Chat chat = chatRepository.findByOwnerAndCustomer(owner, customer);
        Chat chat = chatRepository.findByRestaurantAndCustomer(restaurant, customer);
        if (chat == null) {
            chat = Chat.builder()
                    .customer(customer)
                    .owner(owner)
                    .restaurant(restaurant)
                    .build();
            chatRepository.save(chat);
        }
        return chat.getId();
    }


    public Long messageChat(User user, Long id, String content) {
        User sender = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자가 존재하지 않습니다.", HttpStatus.NOT_FOUND));
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("채팅방이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChat(chat);
        chatMessage.setSender(sender);
        chatMessage.setContent(content);
        chatMessage.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        ChatMessage savedChat = chatMessageRepository.save(chatMessage);

        return savedChat.getId();
    }

    public List<ChatDto> chat(User user, Long id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("채팅방이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChat(chat);

        return chatMessageList.stream()
                .map(chatMessage -> {
                    ChatDto chatDto = new ChatDto();
                    chatDto.setId(chatMessage.getId());
                    chatDto.setChatId(chatMessage.getChat().getId());
                    chatDto.setSender(chatMessage.getSender().getUserName());
                    chatDto.setContent(chatMessage.getContent());
                    chatDto.setDate(chatMessage.getDate());
                    return chatDto;
                })
                .collect(Collectors.toList());
    }
}
