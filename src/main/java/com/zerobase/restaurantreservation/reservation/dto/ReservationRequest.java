package com.zerobase.restaurantreservation.reservation.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    private Integer storeId;
    private String phoneNumber;
    private LocalDateTime reservationTime;
}
