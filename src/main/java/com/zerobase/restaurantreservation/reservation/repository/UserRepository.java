package com.zerobase.restaurantreservation.user.repository;

import com.zerobase.restaurantreservation.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    //  이메일로 사용자 찾기
    Optional<UserEntity> findByEmail(String email);

    //  존재 여부 확인 (회원가입 중복 체크)
    boolean existsByEmail(String email);
}
