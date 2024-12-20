package com.order.controller;

import com.order.common.AuthUser;
import com.order.dto.RestaurantDetailResponseDto;
import com.order.dto.RestaurantDto;
import com.order.dto.RestaurantResponseDto;
import com.order.entity.User;
import com.order.service.GeoCodingService;
import com.order.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final GeoCodingService geoCodingService;

    @GetMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurantById(@AuthUser User user, @PathVariable(value = "id") Long id) {
        RestaurantDetailResponseDto restaurantDetailResponseDto = restaurantService.getRestaurantById(user, id);

        return ResponseEntity.ok(restaurantDetailResponseDto);
    }

    @PostMapping
    public ResponseEntity<?> createRestaurant(@AuthUser User user, @RequestPart(value = "data") RestaurantDto restaurantDto, @RequestPart(value = "file", required = false) MultipartFile file) {
        RestaurantDto restaurant = restaurantService.createRestaurant(restaurantDto, file, user);
        String address = geoCodingService.getCoordinates(restaurant.getId());
        return new ResponseEntity<>(restaurant + address, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable(value = "id") Long id,
                                              @RequestPart(value = "data") RestaurantDto restaurantDto, @RequestPart(value = "file", required = false) MultipartFile file) {
        RestaurantDto restaurant = restaurantService.updateRestaurant(id, restaurantDto, file);
        return ResponseEntity.ok(restaurant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable("id") Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok("Delete successfully");
    }
}
