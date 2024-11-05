package com.order.controller;

import com.order.common.AuthUser;
import com.order.dto.ReviewDto;
import com.order.entity.User;
import com.order.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{id}")
    public ResponseEntity<?> createReview(@AuthUser User user, @PathVariable("id")Long id,
                                          @RequestPart(value = "data") ReviewDto reviewDto) {
        Long reviewId =  reviewService.createReview(user, id, reviewDto);
        return ResponseEntity.ok("reviewId: " + reviewId);
    }

    @GetMapping()
    public ResponseEntity<?> getAllByUser(@AuthUser User user) {
        List<ReviewDto> reviewDto = reviewService.getAllByUser(user);
        return ResponseEntity.ok(reviewDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateReview(@AuthUser User user, @PathVariable("id") long id,
                                          @RequestPart(value = "data") ReviewDto reviewDto) {
        long reviewId = reviewService.updateReview(user, id, reviewDto);
        if(reviewId == -1){
            return ResponseEntity.badRequest().body("작성자만 수정할 수 있습니다.");
        }
        return ResponseEntity.ok(reviewId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@AuthUser User user, @PathVariable("id") long id) {
        long reviewId = reviewService.deleteReview(user, id);
        if(reviewId == -1){
            return ResponseEntity.badRequest().body("작성자만 수정할 수 있습니다.");
        }
        return ResponseEntity.ok(reviewId);
    }
}
