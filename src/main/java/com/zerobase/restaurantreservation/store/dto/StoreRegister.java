package com.zerobase.restaurantreservation.store.dto;

import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import lombok.*;

import java.time.LocalDateTime;

public class StoreRegister {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String managerName;
        private String restaurantName;
        private String restaurantAddress;
        private String restaurantDetail;
        private double lat;
        private double lnt;

        public StoreEntity toEntity() {
            return StoreEntity.builder()
                    .managerName(this.managerName)
                    .restaurantName(this.restaurantName)
                    .restaurantAddress(this.restaurantAddress)
                    .restaurantDetail(this.restaurantDetail)
                    .lat(this.lat)
                    .lnt(this.lnt)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Integer storeId;
        private String managerName;
        private String restaurantName;
        private String restaurantAddress;
        private String restaurantDetail;
        private double lat;
        private double lnt;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public static Response fromEntity(StoreEntity entity) {
            return Response.builder()
                    .storeId(entity.getStoreId())
                    .managerName(entity.getManagerName())
                    .restaurantName(entity.getRestaurantName())
                    .restaurantAddress(entity.getRestaurantAddress())
                    .restaurantDetail(entity.getRestaurantDetail())
                    .lat(entity.getLat())
                    .lnt(entity.getLnt())
                    .createdAt(entity.getCreatedAt())
                    .modifiedAt(entity.getModifiedAt())
                    .build();
        }
    }
}
