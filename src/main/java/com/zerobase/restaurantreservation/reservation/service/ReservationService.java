package com.zerobase.restaurantreservation.reservation.service;

import com.zerobase.restaurantreservation.reservation.dto.ReservationRequest;
import com.zerobase.restaurantreservation.reservation.dto.ReservationResponse;
import com.zerobase.restaurantreservation.reservation.entity.ReservationEntity;
import com.zerobase.restaurantreservation.reservation.repository.ReservationRepository;
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

    // 예약 생성
    public ReservationResponse createReservation(Integer userId, ReservationRequest request) {
        // 사용자 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 매장 확인
        StoreEntity store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));

        // 예약 생성
        ReservationEntity reservation = ReservationEntity.builder()
                .user(user)
                .store(store)
                .reservationTime(request.getReservationTime())
                .createdAt(LocalDateTime.now())
                .build();

        ReservationEntity savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.fromEntity(savedReservation);
    }

    // 사용자의 예약 목록 조회
    public List<ReservationResponse> getUserReservations(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return reservationRepository.findByUser(user).stream()
                .map(ReservationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 예약 상세 조회
    public ReservationResponse getReservationDetail(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        return ReservationResponse.fromEntity(reservation);
    }

    // 예약 취소
    public void cancelReservation(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        reservationRepository.delete(reservation);
    }
}
