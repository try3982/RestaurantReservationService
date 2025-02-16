package com.zerobase.restaurantreservation.user.controller;

import com.zerobase.restaurantreservation.user.dto.UserResiter;
import com.zerobase.restaurantreservation.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 컨트롤러
 * - 사용자 회원가입 기능을 제공합니다.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/signup")
public class UserController {

    private final UserService userService;

    /**
     * 사용자 회원가입 API
     * - 새로운 사용자를 등록.
     * - 이메일이 이미 존재하는 경우 실패 응답을 반환.
     *
     * @param request 회원가입 요청 정보
     * @return 성공적으로 가입된 사용자 정보 반환
     */
    @PostMapping("/register")
    public ResponseEntity<UserResiter.Response> registerUser(@RequestBody UserResiter.Request request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }
}
