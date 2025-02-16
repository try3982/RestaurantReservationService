package com.zerobase.restaurantreservation.user.service;

import com.zerobase.restaurantreservation.user.dto.UserResiter;
import com.zerobase.restaurantreservation.user.entity.UserEntity;
import com.zerobase.restaurantreservation.user.repository.UserRepositrory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 서비스
 * - 사용자 회원가입 기능
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepositrory userRepository;

    /**
     * 사용자 회원가입 메서드
     * - 이메일 중복 체크 후 신규 사용자 등록
     *
     * @param request 회원가입 요청 정보
     * @return 가입된 사용자 정보
     */
    public UserResiter.Response registerUser(UserResiter.Request request) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(request.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 유저 엔티티 생성 및 저장
        UserEntity userEntity = request.toEntity();
        UserEntity savedUser = userRepository.save(userEntity);

        // 가입된 사용자 정보 반환
        return UserResiter.Response.fromEntity(savedUser);
    }
}
