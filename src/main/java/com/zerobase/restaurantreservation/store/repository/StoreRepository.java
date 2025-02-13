package com.zerobase.restaurantreservation.store.repository;

import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {
    Optional<StoreEntity> findByRestaurantName(String restaurantName);

    //  매장 검색 (이름 또는 주소에 키우드)
    List<StoreEntity> findByRestaurantNameContainingOrRestaurantAddressContaining(String nameKeyword, String addressKeyword);
}
