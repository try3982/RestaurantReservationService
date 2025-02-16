package com.zerobase.restaurantreservation.review.controller;

import com.zerobase.restaurantreservation.review.dto.ReviewRequest;
import com.zerobase.restaurantreservation.review.dto.ReviewResponse;
import com.zerobase.restaurantreservation.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 리뷰 컨트롤러
 * - 리뷰 작성, 수정, 삭제, 조회 기능을 제공합니다.
 */
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 작성 API
     * - 방문 완료된 고객만 리뷰 작성 가능
     *
     * @param customerId 리뷰 작성자 ID
     * @param request 리뷰 요청 DTO (storeId, rating, reviewText)
     * @return 생성된 리뷰 정보
     */
    @PostMapping("/create/{customerId}")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Integer customerId,
            @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(customerId, request));
    }

    /**
     * 리뷰 수정 API
     * - 리뷰 작성자만 수정 가능
     *
     * @param customerId 리뷰 작성자 ID
     * @param reviewId 리뷰 ID
     * @param request 리뷰 요청 DTO (storeId, rating, reviewText)
     * @return 수정된 리뷰 정보
     */
    @PutMapping("/update/{customerId}/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Integer customerId,
            @PathVariable Integer reviewId,
            @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(customerId, reviewId, request));
    }

    /**
     * 리뷰 삭제 API
     * - 리뷰 작성자 또는 매장 관리자만 삭제 가능
     *
     * @param userId 삭제 요청자 ID
     * @param reviewId 삭제할 리뷰 ID
     * @return 삭제 성공 메시지
     */
    @DeleteMapping("/delete/{userId}/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Integer userId,
            @PathVariable Integer reviewId) {
        reviewService.deleteReview(userId, reviewId);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }

    /**
     * 특정 매장의 리뷰 목록 조회 API
     *
     * @param storeId 매장 ID
     * @return 해당 매장의 리뷰 목록
     */
    @GetMapping("/list/store/{storeId}")
    public ResponseEntity<List<ReviewResponse>> getStoreReviews(@PathVariable Integer storeId) {
        return ResponseEntity.ok(reviewService.getReviewsByStore(storeId));
    }

    /**
     * 특정 사용자의 리뷰 목록 조회 API
     *
     * @param customerId 사용자 ID
     * @return 해당 사용자의 리뷰 목록
     */
    @GetMapping("/list/customer/{customerId}")
    public ResponseEntity<List<ReviewResponse>> getCustomerReviews(@PathVariable Integer customerId) {
        return ResponseEntity.ok(reviewService.getReviewsByCustomer(customerId));
    }
}
