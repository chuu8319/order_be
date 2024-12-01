package com.order.repository;

import com.order.entity.Chat;
import com.order.entity.Restaurant;
import com.order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    boolean existsByOwnerAndCustomer(User owner, User customer);
    Chat findByOwnerAndCustomer(User owner, User customer);

    Chat findByRestaurantAndCustomer(Restaurant restaurant, User customer);

}