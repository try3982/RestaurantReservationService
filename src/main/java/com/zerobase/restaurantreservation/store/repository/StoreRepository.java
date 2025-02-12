package com.zerobase.restaurantreservation.store.repository;

import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {
    Optional<StoreEntity> findByRestaurantName(String restaurantName);
}
