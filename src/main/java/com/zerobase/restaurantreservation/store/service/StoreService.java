package com.zerobase.restaurantreservation.store.service;

import com.zerobase.restaurantreservation.store.dto.StoreRegister;
import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
