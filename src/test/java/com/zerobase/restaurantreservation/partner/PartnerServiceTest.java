package com.zerobase.restaurantreservation.partner;


import com.zerobase.restaurantreservation.partner.dto.PartnerRegister;
import com.zerobase.restaurantreservation.partner.entity.PartnerEntity;
import com.zerobase.restaurantreservation.partner.repository.PartnerRepository;
import com.zerobase.restaurantreservation.partner.service.PartnerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional // 테스트 실행 후 자동 롤백
class PartnerServiceTest {

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private PartnerRepository partnerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        partnerRepository.deleteAll(); // 테스트 시작 전 DB 초기화
    }

    @AfterEach
    void tearDown() {
        partnerRepository.deleteAll(); // 테스트 종료 후 DB 정리
    }

    @Test
    @DisplayName("파트너 회원가입 성공 - JPA 저장 확인")
    void registerPartner_Success() {
        // Given: 파트너 회원가입 요청 객체
        PartnerRegister.Request request = new PartnerRegister.Request(
                "partner@example.com", "securePass123", "파트너 유저", "010-5678-1234"
        );

        // When: 회원가입 요청 처리
        PartnerRegister.Response response = partnerService.registerPartner(request);

        // Then: 응답 객체 검증
        assertThat(response).isNotNull();
        assertThat(response.getPartnerId()).isNotNull();
        assertThat(response.getPartnerEmail()).isEqualTo("partner@example.com");
        assertThat(response.getName()).isEqualTo("파트너 유저");
        assertThat(response.getPhone()).isEqualTo("010-5678-1234");

        // DB 데이터 검증
        Optional<PartnerEntity> savedPartner = partnerRepository.findByEmail("partner@example.com");
        assertThat(savedPartner).isPresent();
        assertThat(savedPartner.get().getPartnerId()); // DB 저장된 ID와 응답 ID 일치
        assertThat(savedPartner.get().getEmail()).isEqualTo("partner@example.com");
        assertThat(savedPartner.get().getName()).isEqualTo("파트너 유저");
    }

    @Test
    @DisplayName("파트너 회원가입 실패 - 중복 이메일")
    void registerPartner_Fail_DuplicateEmail() {
        // Given: 이미 존재하는 이메일로 회원가입 요청
        PartnerRegister.Request request = new PartnerRegister.Request(
                "duplicate@example.com", "password123", "중복 파트너", "010-9999-8888"
        );

        // 먼저 사용자를 등록
        partnerService.registerPartner(request);

        // 동일한 이메일로 다시 회원가입 요청하면 예외 발생해야 함
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            partnerService.registerPartner(request);
        });

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");
    }
}
