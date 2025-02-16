package com.zerobase.restaurantreservation.review.service;

import com.zerobase.restaurantreservation.reservation.repository.ReservationRepository;
import com.zerobase.restaurantreservation.reservation.type.ReservationStatus;
import com.zerobase.restaurantreservation.review.dto.ReviewRequest;
import com.zerobase.restaurantreservation.review.dto.ReviewResponse;
import com.zerobase.restaurantreservation.review.entity.ReviewEntity;
import com.zerobase.restaurantreservation.review.repository.ReviewRepository;
import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.store.repository.StoreRepository;
import com.zerobase.restaurantreservation.user.entity.UserEntity;
import com.zerobase.restaurantreservation.user.repository.UserRepositrory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepositrory userRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 리뷰 작성 API
     * - 방문 이력이 있는 고객만 리뷰 작성 가능
     *
     * @param customerId 리뷰 작성자 ID
     * @param request    리뷰 요청 정보 (매장 ID, 평점, 리뷰 내용 등)
     * @return 작성된 리뷰 정보 반환
     */
    @Transactional
    public ReviewResponse createReview(Integer customerId, ReviewRequest request) {
        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        StoreEntity store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));

        // 방문 기록 확인 (CONFIRMED 또는 VISITED 상태의 예약이 존재해야 함)
        boolean hasCompletedReservation = reservationRepository.findByCustomerAndStore(customer, store).stream()
                .anyMatch(reservation -> reservation.getStatus() == ReservationStatus.VISITED);

        if (!hasCompletedReservation) {
            throw new IllegalArgumentException("방문 기록이 없어 리뷰를 작성할 수 없습니다.");
        }

        // 리뷰 저장
        ReviewEntity review = reviewRepository.save(
                ReviewEntity.builder()
                        .customer(customer)
                        .store(store)
                        .rating(request.getRating())
                        .reviewText(request.getReviewText())
                        .build()
        );

        return ReviewResponse.fromEntity(review);
    }

    /**
     * 리뷰 수정 API
     * - 작성자 본인만 수정 가능
     *
     * @param userId    리뷰 수정 요청자 Id
     * @param reviewId  수정할 리뷰 ID
     * @param request   리뷰 수정 요청 정보 (평점, 리뷰 내용 등)
     * @return 수정된 리뷰 정보 반환
     */
    @Transactional
    public ReviewResponse updateReview(Integer userId, Integer reviewId, ReviewRequest request) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        // 작성자 검증
        if (!review.getCustomer().getUserId().equals(userId)) {
            throw new IllegalArgumentException("리뷰를 수정할 권한이 없습니다.");
        }

        // 리뷰 수정
        review.setRating(request.getRating());
        review.setReviewText(request.getReviewText());

        return ReviewResponse.fromEntity(review);
    }

    /**
     * 리뷰 삭제 API
     * - 작성자 또는 매장 관리자만 삭제 가능
     *
     * @param userId   리뷰 삭제 요청자 Id
     * @param reviewId 삭제할 리뷰 Id
     */
    @Transactional
    public void deleteReview(Integer userId, Integer reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        // 삭제 권한 확인 (리뷰 작성자 또는 매장 관리자)
        boolean isAuthor = review.getCustomer().getUserId().equals(userId);
        boolean isManager = storeRepository.findById(review.getStore().getStoreId())
                .map(store -> store.getManagerName().equals(userRepository.findById(userId).get().getName()))
                .orElse(false);

        if (!isAuthor && !isManager) {
            throw new IllegalArgumentException("리뷰 삭제 권한이 없습니다.");
        }

        // 리뷰 삭제
        reviewRepository.delete(review);
    }

    // 특정 매장의 리뷰 목록 조회
    public List<ReviewResponse> getReviewsByStore(Integer storeId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));

        return reviewRepository.findByStore(store).stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 사용자의 리뷰 목록 조회
    public List<ReviewResponse> getReviewsByCustomer(Integer customerId) {
        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return reviewRepository.findByCustomer(customer).stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
