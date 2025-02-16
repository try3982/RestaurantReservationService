package com.zerobase.restaurantreservation.review.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private Integer storeId;
    private Integer rating;
    private String reviewText;
}
