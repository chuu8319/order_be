package com.order.repository;

import com.order.entity.PayMenu;
import com.order.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayMenuRepository extends JpaRepository<PayMenu, Long> {
    List<PayMenu> findByRestaurant(Restaurant restaurant);
}
