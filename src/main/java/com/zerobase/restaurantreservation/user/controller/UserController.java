package com.zerobase.restaurantreservation.user.controller;

import com.zerobase.restaurantreservation.user.dto.UserDto;
import com.zerobase.restaurantreservation.user.dto.UserResiter;
import com.zerobase.restaurantreservation.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@ResponseBody
public class UserController {

    private final UserService userService;

    //유저 회원가입 API
    @PostMapping("signup/register")
    public ResponseEntity<UserResiter.Response> registerUser(@RequestBody UserResiter.Request request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

}
