package com.order.repository;

import com.order.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByRestaurantId(Long restaurantId);
    void deleteByRestaurantId(Long id);
}