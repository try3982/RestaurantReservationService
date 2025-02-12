package com.zerobase.restaurantreservation.store.service;

import com.zerobase.restaurantreservation.store.dto.StoreRegister;
import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreRegister.Response registerStore(StoreRegister.Request request) {
        // 중복된 가게 이름 확인
        if (storeRepository.findByRestaurantName(request.getRestaurantName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 가게 이름입니다.");
        }

        // 가게 엔티티 저장
        StoreEntity storeEntity = request.toEntity();
        StoreEntity savedStore = storeRepository.save(storeEntity);

        return StoreRegister.Response.fromEntity(savedStore);
    }

    // 매장 수정 기능
    @Transactional
    public StoreRegister.Response updateStore(Integer storeId, StoreRegister.Request request) {
        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        // 중복된 가게 이름 확인 (현재 수정 중인 매장은 제외)
        if (storeRepository.findByRestaurantName(request.getRestaurantName())
                .filter(store -> !store.getStoreId().equals(storeId))
                .isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 가게 이름입니다.");
        }

        // 매장 정보 업데이트
        storeEntity.setRestaurantName(request.getRestaurantName());
        storeEntity.setManagerName(request.getManagerName());
        storeEntity.setRestaurantAddress(request.getRestaurantAddress());
        storeEntity.setRestaurantDetail(request.getRestaurantDetail());
        storeEntity.setLat(request.getLat());
        storeEntity.setLnt(request.getLnt());
        storeEntity.setModifiedAt(LocalDateTime.now());

        return StoreRegister.Response.fromEntity(storeEntity);
    }

    // 매장 삭제 기능
    public void deleteStore(Integer storeId) {
        // 1. 매장 존재 여부 확인
        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        // 2. 매장 삭제
        storeRepository.delete(storeEntity);
    }


}
