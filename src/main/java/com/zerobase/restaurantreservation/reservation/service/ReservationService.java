package com.zerobase.restaurantreservation.reservation.service;

import com.zerobase.restaurantreservation.reservation.dto.ReservationRequest;
import com.zerobase.restaurantreservation.reservation.dto.ReservationResponse;
import com.zerobase.restaurantreservation.reservation.entity.ReservationEntity;
import com.zerobase.restaurantreservation.reservation.repository.ReservationRepository;
import com.zerobase.restaurantreservation.reservation.type.ReservationStatus;
import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.store.repository.StoreRepository;
import com.zerobase.restaurantreservation.user.entity.UserEntity;
import com.zerobase.restaurantreservation.user.repository.UserRepositrory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepositrory userRepository;
    private final StoreRepository storeRepository;


    /**
     * 예약 생성
     * - 사용자와 매장이 존재하는지 확인
     * - 과거 시간 예약 불가
     * - 동일한 시간에 중복 예약 불가
     * - 예약 생성 후 저장
     */
    public ReservationResponse createReservation(String phoneNumber, ReservationRequest request) {
        UserEntity customer = userRepository.findByPhone(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        StoreEntity store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));

        if (request.getReservationTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거 시간으로 예약할 수 없습니다.");
        }

        boolean exists = reservationRepository.existsByCustomerAndReservationTime(customer, request.getReservationTime());
        if (exists) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 존재합니다.");
        }

        ReservationEntity reservation = reservationRepository.save(
                ReservationEntity.builder()
                        .store(store)
                        .customer(customer)
                        .reservationTime(request.getReservationTime())
                        .createdAt(LocalDateTime.now())
                        .status(ReservationStatus.CONFIRMED)
                        .build()
        );

        return  ReservationResponse.fromEntity(reservation);
    }

    /**
     * 고객의 예약 목록 조회
     * - 존재하는 사용자 여부 확인
     * - 고객의 예약 목록 반환
     */
    public List<ReservationResponse> getCustomerReservations(Integer customerId) {
        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return reservationRepository.findByCustomer(customer).stream()
                .map(ReservationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 예약 상세 조회
     * - 예약 Id를 기준으로 조회
     * - 존재하지 않는 경우 예외 발생
     */
    public ReservationResponse getReservationDetail(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        return ReservationResponse.fromEntity(reservation);
    }

    /**
     * 예약 취소
     * - 예약 Id를 기준으로 예약을 조회 후 삭제
     */
    public ReservationResponse cancelReservation(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        reservationRepository.delete(reservation);
        return ReservationResponse.fromEntity(reservation);
    }

    /**
     * 방문 확인
     * - 예약이 존재하는지 확인
     * - 이미 방문 확인된 예약인지 체크
     * - 방문 상태로 변경
     */
    @Transactional
    public void confirmVisit(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if (reservation.getStatus() == ReservationStatus.VISITED) {
            throw new IllegalStateException("이미 방문 확인이 완료된 예약입니다.");
        }

        reservation.setStatus(ReservationStatus.VISITED);
        reservation.setVisitedAt(LocalDateTime.now());

        reservationRepository.save(reservation);
    }

    /**
     * 예약 승인
     * - 예약이 존재하는지 확인
     * - 대기 상태(PENDING)인 경우만 승인 가능
     * - 상태를 CONFIRMED로 변경 후 저장
     */
    public ReservationResponse approveReservation(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("예약 승인 불가능한 상태입니다.");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        return ReservationResponse.fromEntity(reservation);
    }

    /**
     * 예약 거절
     * - 예약이 존재하는지 확인
     * - 대기 상태(PENDING)인 경우만 거절 가능
     * - 상태를 REJECTED로 변경 후 저장
     */
    public ReservationResponse rejectReservation(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("예약 거절 불가능한 상태입니다.");
        }

        reservation.setStatus(ReservationStatus.REJECTED);
        reservationRepository.save(reservation);

        return ReservationResponse.fromEntity(reservation);
    }
}
