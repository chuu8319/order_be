package com.order.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.order.dto.RestaurantDto;
import jakarta.persistence.*;
import jakarta.websocket.ClientEndpoint;
import lombok.*;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "restaurant_category")
    private String restaurantCategory;

    @Column(name = "restaurant_phone")
    private String restaurantPhone;

    @Column(name = "restaurant_address")
    private String restaurantAddress;

    @Column(name = "restaurant_image_id")
    private Long restaurantImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // DTO를 엔티티로 변환하는 정적 메서드
    public RestaurantDto toDto() {
        return RestaurantDto.builder()
                .id(this.getId())
                .restaurantName(this.getRestaurantName())
                .restaurantCategory(this.getRestaurantCategory())
                .restaurantPhone(this.getRestaurantPhone())
                .restaurantAddress(this.getRestaurantAddress())
                .restaurantImageId(this.getRestaurantImageId())
                .userId(this.getUser().getId())
                .build();
    }
}
