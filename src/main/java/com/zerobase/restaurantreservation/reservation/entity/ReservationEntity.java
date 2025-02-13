package com.zerobase.restaurantreservation.reservation.entity;

import com.zerobase.restaurantreservation.reservation.type.ReservationStatus;
import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private UserEntity customer; // 예약한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store; // 예약한 매장

    @Column(nullable = false)
    private LocalDateTime reservationTime; // 예약 시간

    @Column(nullable = false)
    private LocalDateTime createdAt; // 예약 생성 시간

    @Column(nullable = true)
    private LocalDateTime visitedAt; // 방문 시간

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.CONFIRMED; // 예약 기본값

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = ReservationStatus.CONFIRMED;
    }
}
