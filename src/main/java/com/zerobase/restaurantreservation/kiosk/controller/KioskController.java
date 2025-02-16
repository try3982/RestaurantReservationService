package com.zerobase.restaurantreservation.kiosk.controller;

import com.zerobase.restaurantreservation.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kiosk")
@RequiredArgsConstructor
public class KioskController {

    private final ReservationService reservationService;

    /**
     * 방문 확인 API
     * - 키오스크에서 방문을 확인하면 예약 상태를 VISITED로 변경
     *
     * @param reservationId 방문 확인할 예약 Id
     * @return 방문 확인 결과 메시지
     */
    @PostMapping("/confirm-visit/{reservationId}")
    public ResponseEntity<String> confirmVisit(@PathVariable Integer reservationId) {
        reservationService.confirmVisit(reservationId);
        return ResponseEntity.ok("방문 확인 완료");
    }
}
