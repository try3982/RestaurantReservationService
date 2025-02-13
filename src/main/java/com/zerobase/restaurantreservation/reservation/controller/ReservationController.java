package com.zerobase.restaurantreservation.reservation.controller;

import com.zerobase.restaurantreservation.reservation.dto.ReservationRequest;
import com.zerobase.restaurantreservation.reservation.dto.ReservationResponse;
import com.zerobase.restaurantreservation.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 생성
    @PostMapping("/create")
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestParam String phoneNumber,
            @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(phoneNumber, request));
    }

    // 사용자의 예약 목록 조회
    @GetMapping("/list/{customerId}")
    public ResponseEntity<List<ReservationResponse>> getCustomerReservations(@PathVariable Integer customerId) {
        return ResponseEntity.ok(reservationService.getCustomerReservations(customerId));
    }

    // 예약 상세 조회
    @GetMapping("/detail/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservationDetail(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.getReservationDetail(reservationId));
    }

    // 예약 취소
    @DeleteMapping("/cancel/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable Integer reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok("예약이 취소되었습니다.");
    }

    // 키오스크 방문 확인
    @PostMapping("/confirm-visit/{reservationId}")
    public ResponseEntity<String> confirmVisit(@PathVariable Integer reservationId) {
        reservationService.confirmVisit(reservationId);
        return ResponseEntity.ok("방문이 확인되었습니다.");
    }
}
