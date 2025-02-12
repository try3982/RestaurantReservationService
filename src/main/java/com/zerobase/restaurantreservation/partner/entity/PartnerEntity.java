package com.zerobase.restaurantreservation.partner.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name ="partners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer partnerId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
