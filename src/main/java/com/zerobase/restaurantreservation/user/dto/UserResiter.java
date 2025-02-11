package com.zerobase.restaurantreservation.user.dto;

import com.zerobase.restaurantreservation.user.entity.UserEntity;
import lombok.*;
import java.time.LocalDateTime;

public class UserResiter {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String userEmail;
        private String password;
        private String name;
        private String phone;

        public UserEntity toEntity() {
            return UserEntity.builder()
                    .email(this.userEmail)
                    .password(this.password) // 보안 필요하면 암호화 추가
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
        private Integer userId;
        private String userEmail;
        private String name;
        private String phone;
        private LocalDateTime createdAt;

        public static Response fromEntity(UserEntity entity) {
            return Response.builder()
                    .userId(entity.getUserId())
                    .userEmail(entity.getEmail())
                    .name(entity.getName())
                    .phone(entity.getPhone())
                    .createdAt(entity.getCreatedAt())
                    .build();
        }
    }


    /**
     * 회원가입 요청
     * {
     *     "email": "test@example.com",
     *     "password": "1234",
     *     "name": "테스트 유저",
     *     "phone": "010-1234-5678"
     * }
     *
     *  회원가입 응답
     *  {
     *     "userId": 1,
     *     "email": "test@example.com",
     *     "name": "테스트 유저",
     *     "phone": "010-1234-5678",
     *     "createdAt": "2024-02-11T12:34:56"
     * }
     */
}
