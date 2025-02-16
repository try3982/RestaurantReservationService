package com.zerobase.restaurantreservation.review.dto;

import com.zerobase.restaurantreservation.review.entity.ReviewEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Integer reviewId;
    private Integer customerId;
    private Integer storeId;
    private Integer rating;
    private String reviewText;
    private LocalDateTime createdAt;

    public static ReviewResponse fromEntity(ReviewEntity entity) {
        return ReviewResponse.builder()
                .reviewId(entity.getReviewId())
                .customerId(entity.getCustomer().getUserId())
                .storeId(entity.getStore().getStoreId())
                .rating(entity.getRating())
                .reviewText(entity.getReviewText())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
