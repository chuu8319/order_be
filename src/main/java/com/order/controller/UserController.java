package com.order.controller;

import com.order.common.AuthUser;
import com.order.dto.JoinDto;
import com.order.dto.OrderDto;
import com.order.entity.User;
import com.order.service.UserService;
import lombok.AllArgsConstructor;
import org.hibernate.sql.results.internal.ResolvedSqlSelection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok("Id: "+id);
    }

    @PatchMapping("/update")
    private ResponseEntity<?> updateUser(@RequestPart(value = "data")JoinDto joinDto, @AuthUser User user) {
        long id = userService.updateUser(user, joinDto);
        if(id == -1) {
            return ResponseEntity.badRequest().body("존재하지 않는 회원입니다.");
        }
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/delete")
    private ResponseEntity<?> deleteUser(@AuthUser User user) {
        long id = userService.deleteUser(user);
        if(id == -1) {
            return ResponseEntity.badRequest().body("존재하지 않는 회원입니다.");
        }
        return ResponseEntity.ok(id);
    }

    @GetMapping("/order/owner")
    private ResponseEntity<?> getOwnerOrder(@AuthUser User user) {
        List<OrderDto> orderDtoList = userService.getOwnerOrder(user);

        return ResponseEntity.ok(orderDtoList);
    }

    @GetMapping("/order/customer")
    private ResponseEntity<?> getCustomerOrder(@AuthUser User user) {
        List<OrderDto> orderDtoList = userService.getCustomerOrder(user);

        return ResponseEntity.ok(orderDtoList);
    }

    @GetMapping("/order/name")
    private ResponseEntity<?> getName(@AuthUser User user) {
        String name = userService.getName(user);

        return ResponseEntity.ok(name);
    }
}
