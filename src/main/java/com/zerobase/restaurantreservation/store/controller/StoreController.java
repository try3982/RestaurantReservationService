package com.zerobase.restaurantreservation.store.controller;

import com.zerobase.restaurantreservation.store.dto.StoreRegister;
import com.zerobase.restaurantreservation.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 매장 수정 API
    @PutMapping("/update/{storeId}")
    public ResponseEntity<StoreRegister.Response> updateStore(
            @PathVariable int storeId,
            @RequestBody StoreRegister.Request request) {
        return ResponseEntity.ok(storeService.updateStore(storeId, request));
    }
    // 매장 삭제 API
    @DeleteMapping("/delete/{storeId}")
    public ResponseEntity<String> deleteStore(@PathVariable Integer storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.ok("매장이 성공적으로 삭제되었습니다.");
    }
    //매장 검색 API
    @GetMapping("/search")
    public ResponseEntity<List<StoreRegister.Response>> searchStores(@RequestParam String keyword) {
        return ResponseEntity.ok(storeService.searchStores(keyword));
    }
    // 매장 상세정보  API
    @GetMapping("/detail/{storeId}")
    public ResponseEntity<StoreRegister.Response> getStoreDetail(@PathVariable Integer storeId) {
        return ResponseEntity.ok(storeService.getStoreDetail(storeId));
    }
}
