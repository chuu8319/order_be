package com.order.service;

import com.order.dto.ReviewDto;
import com.order.entity.Restaurant;
import com.order.entity.Review;
import com.order.entity.User;
import com.order.exception.ResourceNotFoundException;
import com.order.repository.RestaurantRepository;
import com.order.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    public Long createReview(User user, Long id, ReviewDto reviewDto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 가게입니다."));

        Review review = new Review();
        review.setReviewContents(reviewDto.getReviewContents());
        review.setRating(reviewDto.getRating());
        review.setUser(user);
        review.setRestaurant(restaurant);
        Review savedReview = reviewRepository.save(review);
        return savedReview.getId();
    }

    public List<ReviewDto> getAllByUser(User user) {
        List<Review> reviews = reviewRepository.findByUserId(user.getId());
        if (reviews == null || reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found for user with ID: " + user.getId(), HttpStatus.NOT_FOUND);
        }
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        for (Review review : reviews) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setReviewContents(review.getReviewContents());
            reviewDto.setRating(review.getRating());
            reviewDto.setRestaurantName(review.getRestaurant().getRestaurantName());
            reviewDtoList.add(reviewDto);
        }
        return reviewDtoList;
    }

    public long updateReview(User user, Long id, ReviewDto reviewDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("존재하지 않는 리뷰입니다.",HttpStatus.NOT_FOUND));

        if(review.getUser() != user) {
            return -1;
        }

        review.setReviewContents(reviewDto.getReviewContents());
        review.setRating(reviewDto.getRating());
        reviewRepository.save(review);
        return review.getId();
    }

    public long deleteReview(User user, long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("존재하지 않는 리뷰입니다.",HttpStatus.NOT_FOUND));

        if(review.getUser() != user) {
            return -1;
        }

        restaurantRepository.deleteById(id);
        return review.getId();
    }

    public List<ReviewDto> getAllByRestaurant(long id) {
        List<Review> reviews = reviewRepository.findByRestaurantId(id);
        if (reviews == null || reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found for restaurant with ID: " + id, HttpStatus.NOT_FOUND);
        }
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        for (Review review : reviews) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setReviewContents(review.getReviewContents());
            reviewDto.setRating(review.getRating());
            reviewDto.setRestaurantName(review.getRestaurant().getRestaurantName());
            reviewDtoList.add(reviewDto);
        }
        return reviewDtoList;
    }
}
