package com.zerobase.restaurantreservation.reservation.dto;

import com.zerobase.restaurantreservation.reservation.entity.ReservationEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    private Integer reservationId;
    private Integer customerId;
    private Integer storeId;
    private LocalDateTime reservationTime;
    private LocalDateTime createdAt;

    public static ReservationResponse fromEntity(ReservationEntity entity) {
        return ReservationResponse.builder()
                .reservationId(entity.getReservationId())
                .customerId(entity.getCustomer().getUserId())
                .storeId(entity.getStore().getStoreId())
                .reservationTime(entity.getReservationTime())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
