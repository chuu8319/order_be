package com.order.service;

import com.order.dto.*;
import com.order.entity.*;
import com.order.exception.ResourceNotFoundException;
import com.order.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ImageRepository imageRepository;
    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;
    private final ImageService imageService;
    private final GeoCodingService geoCodingService;

    public List<RestaurantResponseDto> getAllRestaurant() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<Image> images = imageRepository.findAll();

        List<RestaurantResponseDto> restaurantResponseDtoList = new ArrayList<>();

        for(Restaurant restaurant : restaurants) {
            RestaurantResponseDto restaurantResponseDto = new RestaurantResponseDto();

            restaurantResponseDto.setRestaurantId(restaurant.getId());
            restaurantResponseDto.setRestaurantName(restaurant.getRestaurantName());
            restaurantResponseDto.setRestaurantCategory(restaurant.getRestaurantCategory());
            restaurantResponseDto.setRestaurantPhone(restaurant.getRestaurantPhone());
            restaurantResponseDto.setRestaurantAddress(restaurant.getRestaurantAddress());

            images.stream()
                    .filter(image -> image.getRestaurant().getId().equals(restaurant.getId()))
                    .findFirst().ifPresent(matchingImage -> restaurantResponseDto.setStoredFilePath(matchingImage.getStoredFilePath()));

            restaurantResponseDtoList.add(restaurantResponseDto);
        }

        return restaurantResponseDtoList;
    }

    private List<RestaurantResponseDto> getAllRestaurantByNameOrCategory(List<Restaurant> restaurants) {
        List<RestaurantResponseDto> restaurantResponseDtoList = new ArrayList<>();

        for(Restaurant restaurant : restaurants) {
            RestaurantResponseDto restaurantResponseDto = new RestaurantResponseDto();
            restaurantResponseDto.setRestaurantId(restaurant.getId());
            restaurantResponseDto.setRestaurantCategory(restaurant.getRestaurantCategory());
            restaurantResponseDto.setRestaurantName(restaurant.getRestaurantName());
            restaurantResponseDto.setRestaurantAddress(restaurant.getRestaurantAddress());

            Optional<Image> imageOptional = imageRepository.findByRestaurantId(restaurant.getId());

            imageOptional.ifPresent(image -> {
                restaurantResponseDto.setStoredFilePath(image.getStoredFilePath());
            });

            restaurantResponseDtoList.add(restaurantResponseDto);
        }

        return restaurantResponseDtoList;
    }

    public List<RestaurantResponseDto> getAllRestaurantByName(String name) {
        List<Restaurant> restaurants = restaurantRepository.findByRestaurantNameContainingIgnoreCase(name);

        return getAllRestaurantByNameOrCategory(restaurants);
    }

    public List<RestaurantResponseDto> getAllRestaurantByCategory(String category) {
        List<Restaurant> restaurants = restaurantRepository.findByRestaurantCategoryContainingIgnoreCase(category);

        return getAllRestaurantByNameOrCategory(restaurants);
    }

    @Transactional(readOnly = true)
    public RestaurantDetailResponseDto getRestaurantById(User user, Long id) {
        RestaurantDetailResponseDto restaurantDetailResponseDto = new RestaurantDetailResponseDto();

        // 레스토랑 데이터 조회
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        // 레스토랑 세부 정보 설정
        RestaurantResponseDto restaurantResponseDto = new RestaurantResponseDto();
        restaurantResponseDto.setRestaurantId(restaurant.getId());
        restaurantResponseDto.setRestaurantCategory(restaurant.getRestaurantCategory());
        restaurantResponseDto.setRestaurantPhone(restaurant.getRestaurantPhone());
        restaurantResponseDto.setRestaurantName(restaurant.getRestaurantName());
        restaurantResponseDto.setRestaurantAddress(restaurant.getRestaurantAddress());

        // 이미지 조회 및 저장된 파일 경로 설정
        Optional<Image> imageOptional = imageRepository.findByRestaurantId(restaurant.getId());
        imageOptional.ifPresent(image -> restaurantResponseDto.setStoredFilePath(image.getStoredFilePath()));

        restaurantDetailResponseDto.setRestaurantResponseDto(restaurantResponseDto);

        // 메뉴 목록 조회 및 설정
        List<Menu> menus = menuRepository.findAllByRestaurantId(id);
        if (menus.isEmpty()) {
            restaurantDetailResponseDto.setMenuResponseDtoList(null);
        } else {
            restaurantDetailResponseDto.setMenuResponseDtoList(menus.stream()
                    .map(menu -> {
                        MenuResponseDto menuResponseDto = new MenuResponseDto();
                        menuResponseDto.setId(menu.getId());
                        menuResponseDto.setMenuName(menu.getMenuName());
                        menuResponseDto.setMenuPrice(menu.getMenuPrice());
                        return menuResponseDto;
                    }).toList());
        }

        // 리뷰 목록 조회 및 설정
        List<Review> reviews = reviewRepository.findByRestaurantId(id);
        if (reviews.isEmpty()) {
            restaurantDetailResponseDto.setReviewResponseDtoList(null);
        } else {
            restaurantDetailResponseDto.setReviewResponseDtoList(reviews.stream()
                    .map(review -> {
                        boolean isUser;
                        isUser = review.getUser() == user;
                        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
                        reviewResponseDto.setReviewContents(review.getReviewContents());
                        reviewResponseDto.setRating(review.getRating());
                        reviewResponseDto.setUserName(review.getUser().getUserName());
                        reviewResponseDto.setUser(isUser);
                        reviewResponseDto.setReviewId(review.getId());
                        return reviewResponseDto;
                    }).collect(Collectors.toList()));
        }

        return restaurantDetailResponseDto;
    }

    public RestaurantDto createRestaurant(RestaurantDto restaurantDto, MultipartFile file) {
        Restaurant restaurant = restaurantDto.toEntity();
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        if(file != null) {
            long imageId = imageService.addImage(savedRestaurant, file);
            savedRestaurant.setRestaurantImageId(imageId);
        } else {
            savedRestaurant.setRestaurantImageId(null);
        }

        restaurantRepository.save(savedRestaurant);

        geoCodingService.getCoordinates(savedRestaurant.getId());

        return savedRestaurant.toDto();
    }

    public RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto, MultipartFile file) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        restaurant.setRestaurantName(restaurantDto.getRestaurantName());
        restaurant.setRestaurantCategory(restaurantDto.getRestaurantCategory());
        restaurant.setRestaurantPhone(restaurantDto.getRestaurantPhone());
        restaurant.setRestaurantAddress(restaurantDto.getRestaurantAddress());

        if(file != null) {
            long imageId = imageService.addImage(restaurant, file);
            restaurant.setRestaurantImageId(imageId);
        } else {
            restaurant.setRestaurantImageId(null);
        }

        restaurantRepository.save(restaurant);

        return restaurant.toDto();
    }

    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        menuRepository.deleteByRestaurantId(restaurant.getId());
        imageRepository.deleteByRestaurantId(restaurant.getId());
        restaurantRepository.delete(restaurant);
        reviewRepository.deleteByRestaurantId(restaurant.getId());
    }

}
