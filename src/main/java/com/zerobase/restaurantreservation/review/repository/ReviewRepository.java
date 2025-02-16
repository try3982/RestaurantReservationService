package com.zerobase.restaurantreservation.review.repository;

import com.zerobase.restaurantreservation.review.entity.ReviewEntity;
import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
    List<ReviewEntity> findByStore(StoreEntity store);
    List<ReviewEntity> findByCustomer(UserEntity customer);
    boolean existsByCustomerAndStore(UserEntity customer, StoreEntity store);
}
