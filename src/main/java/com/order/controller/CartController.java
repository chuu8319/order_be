package com.order.controller;

import com.order.common.AuthUser;
import com.order.entity.Cart;
import com.order.entity.User;
import com.order.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    @PostMapping("/{id}")
    public ResponseEntity<?> addToCart(@AuthUser User user, @PathVariable("id") Long restaurantId, @RequestParam(value = "menuId") Long menuId) {
        long cartItem = cartService.addTOCart(user.getId(), restaurantId, menuId);
        if(cartItem == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("하나의 가게에서만 장바구니를 담을 수 있습니다.");
        }
        return ResponseEntity.ok(cartItem);
    }

    @GetMapping
    public ResponseEntity<?> getAllCart(@AuthUser User user) {
       List<Cart> allCart = cartService.getAllCart(user.getId());
       if(allCart == null) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is Empty");
       }
       return ResponseEntity.ok(allCart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromCart(@AuthUser User user, @PathVariable("id") Long menuId) {
        long cartItem = cartService.removeFromCart(user.getId(), menuId);
        if(cartItem == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is Empty");
        }
        return ResponseEntity.ok(cartItem);
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<?> addCart(@AuthUser User user, @PathVariable("id") Long menuId) {
        long cartItem = cartService.addCart(user.getId(), menuId);
        if(cartItem == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is Empty");
        }
        return ResponseEntity.ok(cartItem);
    }
    @PostMapping("/remove/{id}")
    public ResponseEntity<?> removeCart(@AuthUser User user, @PathVariable("id") Long menuId) {
        long cartItem = cartService.removeCart(user.getId(), menuId);
        if(cartItem == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is Empty");
        }
        return ResponseEntity.ok(cartItem);
    }
}
