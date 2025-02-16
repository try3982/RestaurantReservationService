package com.zerobase.restaurantreservation.review;

import com.zerobase.restaurantreservation.reservation.entity.ReservationEntity;
import com.zerobase.restaurantreservation.reservation.repository.ReservationRepository;
import com.zerobase.restaurantreservation.reservation.type.ReservationStatus;
import com.zerobase.restaurantreservation.review.dto.ReviewRequest;
import com.zerobase.restaurantreservation.review.dto.ReviewResponse;
import com.zerobase.restaurantreservation.review.entity.ReviewEntity;
import com.zerobase.restaurantreservation.review.repository.ReviewRepository;
import com.zerobase.restaurantreservation.review.service.ReviewService;
import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.store.repository.StoreRepository;
import com.zerobase.restaurantreservation.user.entity.UserEntity;
import com.zerobase.restaurantreservation.user.repository.UserRepositrory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepositrory userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Integer customerId;
    private Integer storeId;
    private Integer managerId;
    private String phoneNumber;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        reservationRepository.deleteAll();
        userRepository.deleteAll();
        storeRepository.deleteAll();

        // 고객 생성
        UserEntity customer = userRepository.saveAndFlush(
                UserEntity.builder()
                        .name("홍길동")
                        .email("customer@example.com")
                        .password("password")
                        .phone("010-1234-5678")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        customerId = customer.getUserId();
        phoneNumber = customer.getPhone();

        // 매장 관리자 생성
        UserEntity manager = userRepository.saveAndFlush(
                UserEntity.builder()
                        .name("매장 관리자")
                        .email("manager@example.com")
                        .password("password")
                        .phone("010-8765-4321")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        managerId = manager.getUserId();

        // 매장 생성
        StoreEntity store = storeRepository.saveAndFlush(
                StoreEntity.builder()
                        .managerName(manager.getName())
                        .restaurantName("테스트 식당")
                        .restaurantAddress("서울 강남구")
                        .restaurantDetail("맛있는 음식")
                        .lat(37.5665)
                        .lnt(126.9780)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build()
        );

        storeId = store.getStoreId();
    }

    @Test
    @DisplayName("리뷰 작성 성공 - 예약 후 방문 완료한 고객")
    void createReview_Success() {
        // 예약 생성 및 방문 완료 처리
        ReservationEntity reservation = reservationRepository.saveAndFlush(
                ReservationEntity.builder()
                        .store(storeRepository.findById(storeId).get())
                        .customer(userRepository.findById(customerId).get())
                        .reservationTime(LocalDateTime.now().minusDays(1))
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .visitedAt(LocalDateTime.now().minusDays(1))
                        .status(ReservationStatus.VISITED)
                        .build()
        );

        // 리뷰 요청 생성
        ReviewRequest request = new ReviewRequest(storeId, 5, "맛있어요!");
        ReviewResponse response = reviewService.createReview(customerId, request);

        assertThat(response).isNotNull();
        assertThat(response.getRating()).isEqualTo(5);
        assertThat(response.getReviewText()).isEqualTo("맛있어요!");
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 방문 기록 없는 고객")
    void createReview_Fail_NotVisited() {
        ReviewRequest request = new ReviewRequest(storeId, 5, "맛있어요!");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.createReview(customerId, request));
        assertThat(exception.getMessage()).isEqualTo("방문 기록이 없어 리뷰를 작성할 수 없습니다.");
    }

    @Test
    @DisplayName("리뷰 수정 성공 - 작성자 본인")
    void updateReview_Success() {
        // 리뷰 작성
        ReviewEntity review = reviewRepository.saveAndFlush(
                ReviewEntity.builder()
                        .customer(userRepository.findById(customerId).get())
                        .store(storeRepository.findById(storeId).get())
                        .rating(5)
                        .reviewText("맛있어요!")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        // 리뷰 수정 요청
        ReviewRequest request = new ReviewRequest(storeId, 4, "괜찮았어요.");
        ReviewResponse response = reviewService.updateReview(customerId, review.getReviewId(), request);

        assertThat(response.getRating()).isEqualTo(4);
        assertThat(response.getReviewText()).isEqualTo("괜찮았어요.");
    }

    @Test
    @DisplayName("리뷰 수정 실패 - 작성자가 아닌 경우")
    void updateReview_Fail_NotAuthor() {
        ReviewEntity review = reviewRepository.saveAndFlush(
                ReviewEntity.builder()
                        .customer(userRepository.findById(customerId).get())
                        .store(storeRepository.findById(storeId).get())
                        .rating(5)
                        .reviewText("맛있어요!")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        ReviewRequest request = new ReviewRequest(storeId, 3, "별로였어요.");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.updateReview(managerId, review.getReviewId(), request));
        assertThat(exception.getMessage()).isEqualTo("리뷰를 수정할 권한이 없습니다.");
    }

    @Test
    @DisplayName("리뷰 삭제 성공 - 작성자 또는 매장 관리자")
    void deleteReview_Success() {
        ReviewEntity review = reviewRepository.saveAndFlush(
                ReviewEntity.builder()
                        .customer(userRepository.findById(customerId).get())
                        .store(storeRepository.findById(storeId).get())
                        .rating(5)
                        .reviewText("맛있어요!")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        // 작성자가 삭제
        reviewService.deleteReview(customerId, review.getReviewId());
        assertThat(reviewRepository.findById(review.getReviewId())).isEmpty();

        // 매장 관리자가 삭제
        ReviewEntity newReview = reviewRepository.saveAndFlush(
                ReviewEntity.builder()
                        .customer(userRepository.findById(customerId).get())
                        .store(storeRepository.findById(storeId).get())
                        .rating(5)
                        .reviewText("맛있어요!")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        reviewService.deleteReview(managerId, newReview.getReviewId());
        assertThat(reviewRepository.findById(newReview.getReviewId())).isEmpty();
    }

    @Test
    @DisplayName("리뷰 삭제 실패 - 권한 없음")
    void deleteReview_Fail_NoPermission() {
        ReviewEntity review = reviewRepository.saveAndFlush(
                ReviewEntity.builder()
                        .customer(userRepository.findById(customerId).get())
                        .store(storeRepository.findById(storeId).get())
                        .rating(5)
                        .reviewText("맛있어요!")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        Integer anotherUserId = 9999;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.deleteReview(anotherUserId, review.getReviewId()));
        assertThat(exception.getMessage()).isEqualTo("리뷰 삭제 권한이 없습니다.");
    }
}
