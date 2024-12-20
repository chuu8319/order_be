package com.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
    private String reviewContents;
    private int rating;
    private String userName;
    private boolean isUser;
    private Long reviewId;
}
