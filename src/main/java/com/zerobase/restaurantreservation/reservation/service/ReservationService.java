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

    public List<ReservationResponse> getCustomerReservations(Integer customerId) {
        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return reservationRepository.findByCustomer(customer).stream()
                .map(ReservationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public ReservationResponse getReservationDetail(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        return ReservationResponse.fromEntity(reservation);
    }

    public ReservationResponse cancelReservation(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        reservationRepository.delete(reservation);
        return ReservationResponse.fromEntity(reservation);
    }

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
}
