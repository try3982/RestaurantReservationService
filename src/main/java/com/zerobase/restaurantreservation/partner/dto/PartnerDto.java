package com.zerobase.restaurantreservation.partner.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartnerDto {

    private Integer partnerId;
    private String partnerEmail;
    private String partnerName;
    private String partnerPhone;

    private LocalDateTime createdAt;
}
