package com.zerobase.restaurantreservation.store.service;

import com.zerobase.restaurantreservation.store.dto.StoreRegister;
import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    /**
     * 매장 등록
     * - 새로운 매장을 등록.
     * - 동일한 가게 이름이 이미 존재하는 경우 등록할 수 없음.
     *
     * @param request 매장 등록 요청 정보
     * @return 등록된 매장 정보 반환
     */
    public StoreRegister.Response registerStore(StoreRegister.Request request) {
        if (storeRepository.findByRestaurantName(request.getRestaurantName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 가게 이름입니다.");
        }

        StoreEntity storeEntity = request.toEntity();
        StoreEntity savedStore = storeRepository.save(storeEntity);

        return StoreRegister.Response.fromEntity(savedStore);
    }

    /**
     * 매장 수정
     * - 기존 매장의 정보를 수정
     * - 동일한 가게 이름이 이미 존재하는 경우 수정불가
     *
     * @param storeId 수정할 매장 ID
     * @param request 수정할 매장 정보
     * @return 수정된 매장 정보 반환
     */
    @Transactional
    public StoreRegister.Response updateStore(Integer storeId, StoreRegister.Request request) {
        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        if (storeRepository.findByRestaurantName(request.getRestaurantName())
                .filter(store -> !store.getStoreId().equals(storeId))
                .isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 가게 이름입니다.");
        }

        storeEntity.setRestaurantName(request.getRestaurantName());
        storeEntity.setManagerName(request.getManagerName());
        storeEntity.setRestaurantAddress(request.getRestaurantAddress());
        storeEntity.setRestaurantDetail(request.getRestaurantDetail());
        storeEntity.setLat(request.getLat());
        storeEntity.setLnt(request.getLnt());
        storeEntity.setModifiedAt(LocalDateTime.now());

        return StoreRegister.Response.fromEntity(storeEntity);
    }

    /**
     * 매장 삭제
     * - 특정 매장을 삭제
     *
     * @param storeId 삭제할 매장 ID
     */
    public void deleteStore(Integer storeId) {
        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        storeRepository.delete(storeEntity);
    }

    /**
     * 매장 검색
     * - 특정 키워드(이름, 주소)로 매장을 검색
     *
     * @param keyword 검색할 키워드
     * @return 검색된 매장 목록 반환
     */
    public List<StoreRegister.Response> searchStores(String keyword) {
        List<StoreEntity> stores = storeRepository.findByRestaurantNameContainingOrRestaurantAddressContaining(keyword, keyword);
        return stores.stream()
                .map(StoreRegister.Response::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 매장 상세정보 조회
     * - 특정 매장의 상세 정보를 조회
     *
     * @param storeId 조회할 매장 ID
     * @return 매장 상세 정보 반환
     */
    public StoreRegister.Response getStoreDetail(Integer storeId) {
        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));
        return StoreRegister.Response.fromEntity(storeEntity);
    }

    /**
     * 매장 목록 가나다순 정렬
     *
     * @return 가나다순 정렬된 매장 목록 반환
     */
    public List<StoreEntity> getStoresSortedByName() {
        return storeRepository.findAllByOrderByRestaurantNameAsc();
    }

    /**
     * 매장 목록 별점순 정렬
     *
     * @return 별점순 정렬된 매장 목록 반환
     */
    public List<StoreEntity> getStoresSortedByRating() {
        return storeRepository.findAllByOrderByRatingDesc();
    }

    /**
     * 매장 목록 거리순 정렬
     * - 특정 위치(lat, lnt) 기준으로 거리순 정렬된 매장 목록을 반환
     *
     * @param lat 위도
     * @param lnt 경도
     * @return 거리순 정렬된 매장 목록 반환
     */
    public List<StoreEntity> getStoresSortedByDistance(double lat, double lnt) {
        return storeRepository.findAllByDistance(lat, lnt);
    }
}
