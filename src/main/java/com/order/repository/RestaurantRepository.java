package com.order.repository;

import com.order.entity.Restaurant;
import com.order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByRestaurantNameContainingIgnoreCase(String restaurantName);
    List<Restaurant> findByRestaurantCategoryContainingIgnoreCase(String category);
    List<Restaurant> findByUser(User user);
}
