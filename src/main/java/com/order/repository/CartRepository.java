package com.order.repository;

import com.order.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(long userId);
    Cart findByUserIdAndMenuId(Long userId, Long menuId);
}
