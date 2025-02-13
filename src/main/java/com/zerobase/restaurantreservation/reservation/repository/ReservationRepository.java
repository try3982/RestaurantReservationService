package com.zerobase.restaurantreservation.reservation.repository;

import com.zerobase.restaurantreservation.reservation.entity.ReservationEntity;
import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
    List<ReservationEntity> findByCustomer(UserEntity customer);

    //  특정 시간에 동일 매장에 예약이 있는지 확인하는 메서등빈다
    boolean existsByStoreAndReservationTime(StoreEntity store, LocalDateTime reservationTime);

    // 예약 정보 확인
    boolean existsByCustomerAndReservationTime(UserEntity customer, LocalDateTime reservationTime);

}
