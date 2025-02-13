package com.zerobase.restaurantreservation.store.repository;

import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {
    Optional<StoreEntity> findByRestaurantName(String restaurantName);

    //  매장 검색 (이름 또는 주소에 키우드)
    List<StoreEntity> findByRestaurantNameContainingOrRestaurantAddressContaining(String nameKeyword, String addressKeyword);

    // 매장 가나다 순 정령
    List<StoreEntity> findAllByOrderByRestaurantNameAsc();

    //별점순 정렬
    List<StoreEntity> findAllByOrderByRatingDesc();

    // 거리순 정렬
    @Query(value = """
        SELECT *, 
            (6371 * acos(
                cos(radians(:lat)) * cos(radians(s.lat)) * 
                cos(radians(s.lnt) - radians(:lnt)) + 
                sin(radians(:lat)) * sin(radians(s.lat))
            )) AS distance
        FROM store s
        ORDER BY distance ASC
    """, nativeQuery = true)
    List<StoreEntity> findAllByDistance(@Param("lat") double lat, @Param("lnt") double lnt);
}
