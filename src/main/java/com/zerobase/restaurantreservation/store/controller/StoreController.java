package com.zerobase.restaurantreservation.store.controller;

import com.zerobase.restaurantreservation.store.dto.StoreRegister;
import com.zerobase.restaurantreservation.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;

    // 매장 등록 API
    @PostMapping("/register")
    public ResponseEntity<StoreRegister.Response> registerStore(@RequestBody StoreRegister.Request request) {
        return ResponseEntity.ok(storeService.registerStore(request));
    }
}
