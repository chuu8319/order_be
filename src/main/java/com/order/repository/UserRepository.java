package com.order.repository;

import com.order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByUserId(String userId);
    User findByUserId(String userId);
}