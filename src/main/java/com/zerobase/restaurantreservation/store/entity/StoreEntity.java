package com.zerobase.restaurantreservation.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeId;

    @Column(nullable = false)
    private String managerName;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String restaurantAddress;  // 레스토랑 주소

    private String restaurantDetail;

    @Column(nullable = false)
    private double lat;  // 위도

    @Column(nullable = false)
    private double lnt;  // 경도

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 생성일시

    @Column(nullable = false)
    private LocalDateTime modifiedAt;  // 수정일시

    // 엔티티 저장 전에 `createdAt`, `modifiedAt` 자동 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
}
