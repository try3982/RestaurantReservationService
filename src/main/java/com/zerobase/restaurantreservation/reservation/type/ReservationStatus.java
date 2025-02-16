package com.zerobase.restaurantreservation.reservation.type;

public enum ReservationStatus {
    PENDING,  // 예약 대기 (r기본값)
    CONFIRMED, // 예약 확정 (점장이 승인하면 )
    REJECTED, // 예약 거절 (점장이 거절하면)
    VISITED, // 방문 완료
    CANCELLED // 취소됨
}
