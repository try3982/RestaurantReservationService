package com.zerobase.restaurantreservation.partner.repository;

import com.zerobase.restaurantreservation.partner.entity.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {
    Optional<PartnerEntity> findByEmail(String email);
}
