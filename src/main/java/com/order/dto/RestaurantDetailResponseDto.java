package com.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDetailResponseDto {
    RestaurantResponseDto restaurantResponseDto;
    List<MenuResponseDto> menuResponseDtoList;
    List<ReviewResponseDto> reviewResponseDtoList;
}
