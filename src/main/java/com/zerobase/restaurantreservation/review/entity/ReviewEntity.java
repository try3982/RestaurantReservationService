package com.zerobase.restaurantreservation.review.entity;

import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private UserEntity customer;  // 리뷰 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;  // 리뷰 대상 매장

    @Column(nullable = false)
    private Integer rating;  // 평점 (1~5)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reviewText;  // 리뷰 내용

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 리뷰 작성 시간

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
