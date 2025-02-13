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
    private Integer storeId;
    private String storeName;
    private LocalDateTime reservationTime;
    private LocalDateTime createdAt;

    public static ReservationResponse fromEntity(ReservationEntity entity) {
        return ReservationResponse.builder()
                .reservationId(entity.getReservationId())
                .storeId(entity.getStore().getStoreId())
                .storeName(entity.getStore().getRestaurantName())
                .reservationTime(entity.getReservationTime())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
