package com.order.repository;

import com.order.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByRestaurantId(Long id);
    void deleteByRestaurantId(Long id);
    Menu findByMenuName(String menu);

    Menu findByMenuNameAndRestaurantId(String menu, Long id);
}