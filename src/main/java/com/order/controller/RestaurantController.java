package com.order.controller;

import com.order.dto.RestaurantDetailResponseDto;
import com.order.dto.RestaurantDto;
import com.order.dto.RestaurantResponseDto;
import com.order.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class RestaurantController {
    private RestaurantService restaurantService;

    @GetMapping("/restaurant")
    public ResponseEntity<List<?>> getAllRestaurant(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "category", required = false) String category) {

        List<RestaurantResponseDto> restaurantResponseDtoList;

        if (name != null && !name.isEmpty()) {
            restaurantResponseDtoList = restaurantService.getAllRestaurantByName(name);
        } else if (category != null && !category.isEmpty()) {
            restaurantResponseDtoList = restaurantService.getAllRestaurantByCategory(category);
        } else {
            restaurantResponseDtoList = restaurantService.getAllRestaurant();
        }

        return  ResponseEntity.ok(restaurantResponseDtoList);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable(value = "id") Long id) {
        RestaurantDetailResponseDto restaurantDetailResponseDto = restaurantService.getRestaurantById(id);

        return ResponseEntity.ok(restaurantDetailResponseDto);
    }

    @PostMapping("/restaurant")
    public ResponseEntity<?> createRestaurant(@RequestPart(value = "data") RestaurantDto restaurantDto, @RequestPart(value = "file", required = false) MultipartFile file) {
        RestaurantDto restaurant = restaurantService.createRestaurant(restaurantDto, file);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

}
