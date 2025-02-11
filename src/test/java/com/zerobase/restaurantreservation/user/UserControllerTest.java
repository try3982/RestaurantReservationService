package com.zerobase.restaurantreservation.user;

import com.zerobase.restaurantreservation.user.dto.UserResiter;
import com.zerobase.restaurantreservation.user.entity.UserEntity;
import com.zerobase.restaurantreservation.user.repository.UserRepositrory;
import com.zerobase.restaurantreservation.user.service.UserService;
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
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepositrory userRepositrory;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        userRepositrory.deleteAll(); // 테스트 시작 전 DB 초기화
    }

    @AfterEach
    void tearDown() {
        userRepositrory.deleteAll(); // 테스트 종료 후 DB 정리
    }

    @Test
    @DisplayName("회원가입 성공 - JPA 저장 확인")
    void registerUser_Success() {
        // Given: 회원가입 요청 객체
        UserResiter.Request request = new UserResiter.Request(
                "test@example.com", "password123", "테스트 유저", "010-1234-5678"
        );

        // When: 회원가입 요청 처리
        UserResiter.Response response = userService.registerUser(request);

        // Then: 응답 객체 검증
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isNotNull();
        assertThat(response.getUserEmail()).isEqualTo("test@example.com");
        assertThat(response.getName()).isEqualTo("테스트 유저");
        assertThat(response.getPhone()).isEqualTo("010-1234-5678");

        // DB 데이터 검증
        Optional<UserEntity> savedUser = userRepositrory.findByEmail("test@example.com"); // 수정된 메서드 사용
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getUserId()).isEqualTo(response.getUserId()); // DB 저장된 ID와 응답 ID 일치
        assertThat(savedUser.get().getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.get().getName()).isEqualTo("테스트 유저");
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 이메일")
    void registerUser_Fail_DuplicateEmail() {
        // Given: 이미 존재하는 이메일로 회원가입 요청
        UserResiter.Request request = new UserResiter.Request(
                "duplicate@example.com", "password123", "중복 사용자", "010-1111-2222"
        );

        // 먼저 사용자를 등록
        userService.registerUser(request);

        // 동일한 이메일로 다시 회원가입 요청하면 예외 발생해야 함
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(request);
        });

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");
    }
}
