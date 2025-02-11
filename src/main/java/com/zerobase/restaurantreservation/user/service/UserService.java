package com.zerobase.restaurantreservation.user.service;

import com.zerobase.restaurantreservation.user.dto.UserDto;
import com.zerobase.restaurantreservation.user.dto.UserResiter;
import com.zerobase.restaurantreservation.user.entity.UserEntity;
import com.zerobase.restaurantreservation.user.repository.UserRepositrory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepositrory userRepository;

    public UserResiter.Response registerUser(UserResiter.Request request) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(request.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 유저 엔티티 저장
        UserEntity userEntity = request.toEntity();
        UserEntity savedUser = userRepository.save(userEntity);

        // Response 반환
        return UserResiter.Response.fromEntity(savedUser);
    }
}