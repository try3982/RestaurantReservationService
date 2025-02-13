package com.zerobase.restaurantreservation.reservation;

import com.zerobase.restaurantreservation.reservation.dto.ReservationRequest;
import com.zerobase.restaurantreservation.reservation.dto.ReservationResponse;
import com.zerobase.restaurantreservation.reservation.entity.ReservationEntity;
import com.zerobase.restaurantreservation.reservation.repository.ReservationRepository;
import com.zerobase.restaurantreservation.reservation.service.ReservationService;
import com.zerobase.restaurantreservation.reservation.type.ReservationStatus;
import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.store.repository.StoreRepository;
import com.zerobase.restaurantreservation.user.entity.UserEntity;
import com.zerobase.restaurantreservation.user.repository.UserRepositrory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepositrory userRepository;

    @Autowired
    private StoreRepository storeRepository;

    private Integer customerId;
    private Integer storeId;
    private String phoneNumber;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        userRepository.deleteAll();
        storeRepository.deleteAll();

        System.out.println("🛠 모든 데이터 삭제 완료 → 예약 수: " + reservationRepository.count());
        System.out.println("🛠 모든 데이터 삭제 완료 → 사용자 수: " + userRepository.count());
        System.out.println("🛠 모든 데이터 삭제 완료 → 매장 수: " + storeRepository.count());

        // 중복 방지를 위해 동적으로 이메일 생성
        String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";

        // 고객 생성
        UserEntity customer = userRepository.saveAndFlush(
                UserEntity.builder()
                        .name("홍길동")
                        .email(uniqueEmail)
                        .password("password")
                        .phone("010-1234-5678")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        customerId = customer.getUserId();
        phoneNumber = customer.getPhone();

        // 매장 생성
        StoreEntity store = storeRepository.saveAndFlush(
                StoreEntity.builder()
                        .managerName("매니저1")
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
    @DisplayName("예약 생성 성공")
    public void createReservation_Success() {
        ReservationRequest request = new ReservationRequest(storeId, phoneNumber, LocalDateTime.now().plusDays(1));
        ReservationResponse response = reservationService.createReservation(phoneNumber, request);

        assertThat(response).isNotNull();
        assertThat(response.getCustomerId()).isEqualTo(customerId);
        assertThat(response.getStoreId()).isEqualTo(storeId);
    }

    @Test
    @DisplayName("예약 생성 실패 - 존재하지 않는 사용자")
    void createReservation_Fail_CustomerNotFound() {
        ReservationRequest request = new ReservationRequest(storeId, "010-9999-9999", LocalDateTime.now().plusDays(1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reservationService.createReservation("010-9999-9999", request));
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 사용자입니다.");
    }

    @Test
    @DisplayName("예약 생성 실패 - 존재하지 않는 매장")
    void createReservation_Fail_StoreNotFound() {
        ReservationRequest request = new ReservationRequest(9999, phoneNumber, LocalDateTime.now().plusDays(1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reservationService.createReservation(phoneNumber, request));
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 매장입니다.");
    }

    @Test
    @DisplayName("예약 생성 실패 - 과거 시간 예약")
    void createReservation_Fail_PastTime() {
        ReservationRequest request = new ReservationRequest(storeId, phoneNumber, LocalDateTime.now().minusDays(1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reservationService.createReservation(phoneNumber, request));
        assertThat(exception.getMessage()).isEqualTo("과거 시간으로 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("예약 생성 실패 - 동일 시간 중복 예약")
    void createReservation_Fail_DuplicateReservation() {
        ReservationRequest request = new ReservationRequest(storeId, phoneNumber, LocalDateTime.now().plusDays(1));

        reservationService.createReservation(phoneNumber, request);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reservationService.createReservation(phoneNumber, request));
        assertThat(exception.getMessage()).isEqualTo("이미 해당 시간에 예약이 존재합니다.");
    }

    @Test
    @DisplayName("고객의 예약 목록 조회")
    void getCustomerReservations_Success() {
        ReservationRequest request1 = new ReservationRequest(storeId, phoneNumber, LocalDateTime.now().plusDays(1));
        ReservationRequest request2 = new ReservationRequest(storeId, phoneNumber, LocalDateTime.now().plusDays(2));

        reservationService.createReservation(phoneNumber, request1);
        reservationService.createReservation(phoneNumber, request2);

        List<ReservationResponse> reservations = reservationService.getCustomerReservations(customerId);
        assertThat(reservations).hasSize(2);
    }

    @Test
    @DisplayName("예약 상세 조회 성공")
    void getReservationDetail_Success() {
        ReservationRequest request = new ReservationRequest(storeId, phoneNumber, LocalDateTime.now().plusDays(1));
        ReservationResponse createdReservation = reservationService.createReservation(phoneNumber, request);

        ReservationResponse response = reservationService.getReservationDetail(createdReservation.getReservationId());

        assertThat(response).isNotNull();
        assertThat(response.getReservationId()).isEqualTo(createdReservation.getReservationId());
    }

    @Test
    @DisplayName("예약 취소 성공")
    void cancelReservation_Success() {
        ReservationRequest request = new ReservationRequest(storeId, phoneNumber, LocalDateTime.now().plusDays(1));
        ReservationResponse createdReservation = reservationService.createReservation(phoneNumber, request);

        reservationService.cancelReservation(createdReservation.getReservationId());

        Optional<ReservationEntity> deletedReservation = reservationRepository.findById(createdReservation.getReservationId());
        assertThat(deletedReservation).isEmpty();
    }

    @Test
    @DisplayName("매장 방문 확인 성공")
    void confirmVisit_Success() {
        ReservationRequest request = new ReservationRequest(storeId, phoneNumber, LocalDateTime.now().plusDays(1));
        ReservationResponse createdReservation = reservationService.createReservation(phoneNumber, request);

        reservationService.confirmVisit(createdReservation.getReservationId());

        ReservationEntity updatedReservation = reservationRepository.findById(createdReservation.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("예약이 존재하지 않습니다."));

        assertThat(updatedReservation.getStatus()).isEqualTo(ReservationStatus.VISITED);
        assertThat(updatedReservation.getVisitedAt()).isNotNull();
    }

    @Test
    @DisplayName("매장 방문 확인 실패 - 존재하지 않는 예약")
    void confirmVisit_Fail_NotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reservationService.confirmVisit(9999));
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 예약입니다.");
    }
}
