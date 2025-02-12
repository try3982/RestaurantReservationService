package com.zerobase.restaurantreservation.partner.service;

import com.zerobase.restaurantreservation.partner.dto.PartnerRegister;
import com.zerobase.restaurantreservation.partner.entity.PartnerEntity;
import com.zerobase.restaurantreservation.partner.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PartnerService {
    private  final PartnerRepository partnerRepository;

    public PartnerRegister.Response registerPartner(PartnerRegister.Request request) {
        // 이메일 중복 체크
        if (partnerRepository.findByEmail(request.getPartnerEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 파트너 엔티티 저장
        PartnerEntity partnerEntity = request.toEntity();
        PartnerEntity savedPartner = partnerRepository.save(partnerEntity);

        // Response 반환
        return PartnerRegister.Response.fromEntity(savedPartner);
    }

}
