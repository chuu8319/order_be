package com.order.controller;

import com.order.dto.JoinDto;
import com.order.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    private ResponseEntity<?> join(@RequestPart(value = "data") JoinDto joinDto) {
        long id = userService.joinUser(joinDto);
        if(id == -1) {
            return ResponseEntity.badRequest().body("이미 존재하는 회원입니다.");
        }
        return ResponseEntity.ok("userId: "+id);
    }

}
