package com.order.dto;

import com.order.entity.Restaurant;
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
    private Long restaurantOwner;


    // DTO에서 엔티티로 변환하는 메서드
    public Restaurant toEntity() {
        return Restaurant.builder()
                .id(this.id)
                .restaurantName(this.restaurantName)
                .restaurantCategory(this.restaurantCategory)
                .restaurantPhone(this.restaurantPhone)
                .restaurantAddress(this.restaurantAddress)
                .restaurantImageId(this.restaurantImageId)
                .restaurantOwner(this.restaurantOwner)
                .build();
    }
}
