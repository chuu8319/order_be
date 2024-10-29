package com.order.dto;

import com.order.entity.Restaurant;
import com.order.entity.User; // User 클래스 임포트
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantDto {
    private Long id;
    private String restaurantName;
    private String restaurantCategory;
    private String restaurantPhone;
    private String restaurantAddress;
    private Long restaurantImageId;
    private Long userId;

    // DTO에서 엔티티로 변환하는 메서드
    public Restaurant toEntity() {
        User user = new User(); // User 객체 생성
        user.setId(this.userId); // userId 설정

        return Restaurant.builder()
                .id(this.id)
                .restaurantName(this.restaurantName)
                .restaurantCategory(this.restaurantCategory)
                .restaurantPhone(this.restaurantPhone)
                .restaurantAddress(this.restaurantAddress)
                .restaurantImageId(this.restaurantImageId)
                .user(user) // User 객체 설정
                .build();
    }
}