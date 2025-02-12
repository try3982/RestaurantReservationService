package com.zerobase.restaurantreservation.partner.dto;

import com.zerobase.restaurantreservation.partner.entity.PartnerEntity;
import lombok.*;

import java.time.LocalDateTime;

public class PartnerRegiter {


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String partnerEmail;
        private String password;
        private String name;
        private String phone;

        public PartnerEntity toEntity() {
            return PartnerEntity.builder()
                    .email(this.partnerEmail)
                    .password(this.password)
                    .name(this.name)
                    .phone(this.phone)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Integer partnerId;
        private String partnerEmail;
        private String name;
        private String phone;
        private LocalDateTime createdAt;

        public static PartnerRegiter.Response fromEntity(PartnerEntity entity) {
            return Response.builder()
                    .partnerId(entity.getPartnerId())
                    .partnerEmail(entity.getEmail())
                    .name(entity.getName())
                    .phone(entity.getPhone())
                    .createdAt(entity.getCreatedAt())
                    .build();
        }
    }


}
