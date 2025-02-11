package com.zerobase.restaurantreservation.user.repository;

import com.zerobase.restaurantreservation.user.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositrory extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
