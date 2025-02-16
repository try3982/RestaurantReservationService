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

    /**
     * 파트너 회원가입 처리
     * - 이메일 중복 여부 확인 후 가입 진행
     * - 중복 이메일 존재 시 예외 발생
     * - 가입 성공 시 저장된 파트너 정보 반환
     */
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
