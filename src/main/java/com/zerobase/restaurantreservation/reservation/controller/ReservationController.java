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
    @PostMapping("/create/{userId}")
    public ResponseEntity<ReservationResponse> createReservation(
            @PathVariable Integer userId,
            @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(userId, request));
    }

    // 사용자의 예약 목록 조회
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<ReservationResponse>> getUserReservations(@PathVariable Integer userId) {
        return ResponseEntity.ok(reservationService.getUserReservations(userId));
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
}
