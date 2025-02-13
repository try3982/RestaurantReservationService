package com.zerobase.restaurantreservation.reservation.service;

import com.zerobase.restaurantreservation.reservation.dto.ReservationRequest;
import com.zerobase.restaurantreservation.reservation.dto.ReservationResponse;
import com.zerobase.restaurantreservation.reservation.entity.ReservationEntity;
import com.zerobase.restaurantreservation.reservation.repository.ReservationRepository;
import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.store.repository.StoreRepository;
import com.zerobase.restaurantreservation.user.entity.UserEntity;
import com.zerobase.restaurantreservation.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    // 예약 생성
    public ReservationResponse createReservation(Integer customerId, ReservationRequest request) {
        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        StoreEntity store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));

        if (request.getReservationTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거 시간으로 예약할 수 없습니다.");
        }

        boolean isDuplicate = reservationRepository.existsByStoreAndReservationTime(store, request.getReservationTime());
        if (isDuplicate) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 존재합니다.");
        }

        ReservationEntity reservation = ReservationEntity.builder()
                .customer(customer)
                .store(store)
                .reservationTime(request.getReservationTime())
                .createdAt(LocalDateTime.now())
                .build();

        ReservationEntity savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.fromEntity(savedReservation);
    }

    // 사용자의 예약 목록 조회
    public List<ReservationResponse> getCustomerReservations(Integer customerId) {
        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return reservationRepository.findByCustomer(customer).stream()
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
    public ReservationResponse cancelReservation(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        reservationRepository.delete(reservation);
        return ReservationResponse.fromEntity(reservation);
    }
}
