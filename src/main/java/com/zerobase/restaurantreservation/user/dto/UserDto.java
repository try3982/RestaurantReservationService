package com.zerobase.restaurantreservation.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    //회원 정보
    private Integer userId;
    private String userEmail;
    private String userPassword;
    private String userName;
    private String userPhone;

    private LocalDateTime createdAt;


}
