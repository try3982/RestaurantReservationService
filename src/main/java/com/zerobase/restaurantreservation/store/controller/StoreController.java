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

    /**
     * 매장 등록 API
     * - 매장을 새롭게 등록하는 API
     *
     * @param request 매장 등록 요청 정보
     * @return 등록된 매장 정보 반환
     */
    @PostMapping("/register")
    public ResponseEntity<StoreRegister.Response> registerStore(@RequestBody StoreRegister.Request request) {
        return ResponseEntity.ok(storeService.registerStore(request));
    }

    /**
     * 매장 수정 API
     * - 기존 매장 정보를 수정하는 API
     *
     * @param storeId 수정할 매장 ID
     * @param request 매장 수정 요청 정보
     * @return 수정된 매장 정보 반환
     */
    @PutMapping("/update/{storeId}")
    public ResponseEntity<StoreRegister.Response> updateStore(
            @PathVariable int storeId,
            @RequestBody StoreRegister.Request request) {
        return ResponseEntity.ok(storeService.updateStore(storeId, request));
    }

    /**
     * 매장 삭제 API
     * - 특정 매장을 삭제하는 API
     *
     * @param storeId 삭제할 매장 ID
     * @return 성공 메시지 반환
     */
    @DeleteMapping("/delete/{storeId}")
    public ResponseEntity<String> deleteStore(@PathVariable Integer storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.ok("매장이 성공적으로 삭제되었습니다.");
    }

    /**
     * 매장 검색 API
     * - 특정 키워드로 매장을 검색하는 API
     *
     * @param keyword 검색 키워드
     * @return 검색된 매장 목록 반환
     */
    @GetMapping("/search")
    public ResponseEntity<List<StoreRegister.Response>> searchStores(@RequestParam String keyword) {
        return ResponseEntity.ok(storeService.searchStores(keyword));
    }

    /**
     * 매장 상세정보 API
     * - 특정 매장의 상세 정보를 조회하는 API
     *
     * @param storeId 조회할 매장 ID
     * @return 매장 상세 정보 반환
     */
    @GetMapping("/detail/{storeId}")
    public ResponseEntity<StoreRegister.Response> getStoreDetail(@PathVariable Integer storeId) {
        return ResponseEntity.ok(storeService.getStoreDetail(storeId));
    }
}
