package com.order.repository;

import com.order.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRestaurantId(Long id);

    List<Review> findByUserId(Long id);

    void deleteByRestaurantId(Long id);
}