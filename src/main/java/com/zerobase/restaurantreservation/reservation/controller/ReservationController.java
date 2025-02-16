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

    /**
     * 예약 생성 API
     * - 사용자가 매장을 예약하는 기능
     *
     * @param phoneNumber 사용자 전화번호
     * @param request     예약 요청 정보
     * @return 생성된 예약 정보 반환
     */
    @PostMapping("/create")
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestParam String phoneNumber,
            @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(phoneNumber, request));
    }

    /**
     * 사용자의 예약 목록 조회 API
     * - 특정 사용자의 모든 예약 내역 조회
     *
     * @param customerId 고객 Id
     * @return 해당 고객의 예약 목록 반환
     */
    @GetMapping("/list/{customerId}")
    public ResponseEntity<List<ReservationResponse>> getCustomerReservations(@PathVariable Integer customerId) {
        return ResponseEntity.ok(reservationService.getCustomerReservations(customerId));
    }

    /**
     * 예약 상세 조회 API
     * - 특정 예약의 상세 정보를 반환
     *
     * @param reservationId 예약 Id
     * @return 예약 상세 정보 반환
     */
    @GetMapping("/detail/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservationDetail(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.getReservationDetail(reservationId));
    }

    /**
     * 예약 취소 API
     * - 예약을 취소하는 기능
     *
     * @param reservationId 예약 Id
     * @return 성공 메시지 반환
     */
    @DeleteMapping("/cancel/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable Integer reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok("예약이 취소되었습니다.");
    }

    /**
     * 방문 확인 API (키오스크 기능)
     * - 예약자가 매장에 방문했음을 확인하는 기능
     *
     * @param reservationId 예약 Id
     * @return 방문 확인 성공 메시지 반환
     */
    @PostMapping("/confirm-visit/{reservationId}")
    public ResponseEntity<String> confirmVisit(@PathVariable Integer reservationId) {
        reservationService.confirmVisit(reservationId);
        return ResponseEntity.ok("방문이 확인되었습니다.");
    }

    /**
     * 예약 승인 API
     * - 점장이 예약 요청을 승인하는 기능
     *
     * @param reservationId 예약 Id
     * @return 승인된 예약 정보 반환
     */
    @PutMapping("/approve/{reservationId}")
    public ResponseEntity<ReservationResponse> approveReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.approveReservation(reservationId));
    }

    /**
     * 예약 거절 API
     * - 점장이 예약 요청을 거절하는 기능
     *
     * @param reservationId 예약 Id
     * @return 거절된 예약 정보 반환
     */
    @PutMapping("/reject/{reservationId}")
    public ResponseEntity<ReservationResponse> rejectReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.rejectReservation(reservationId));
    }
}
