package com.zerobase.restaurantreservation.stores;

import com.zerobase.restaurantreservation.store.dto.StoreRegister;
import com.zerobase.restaurantreservation.store.entity.StoreEntity;
import com.zerobase.restaurantreservation.store.repository.StoreRepository;
import com.zerobase.restaurantreservation.store.service.StoreService;
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
class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        storeRepository.deleteAll(); // 테스트 시작 전 DB 초기화
    }

    @AfterEach
    void tearDown() {
        storeRepository.deleteAll(); // 테스트 종료 후 DB 정리
    }

    @Test
    @DisplayName("매장 등록 성공 - JPA 저장 확인")
    void registerStore_Success() {
        // Given: 매장 등록 요청 객체
        StoreRegister.Request request = new StoreRegister.Request(
                "홍길동",
                "맛있는 식당",
                "서울시 강남구 테헤란로 123",
                "강남역 3번 출구 근처",
                37.498095,
                127.027610
        );

        // When: 매장 등록 요청 처리
        StoreRegister.Response response = storeService.registerStore(request);

        // Then: 응답 객체 검증
        assertThat(response).isNotNull();
        assertThat(response.getStoreId()).isNotNull();
        assertThat(response.getManagerName()).isEqualTo("홍길동");
        assertThat(response.getRestaurantName()).isEqualTo("맛있는 식당");
        assertThat(response.getRestaurantAddress()).isEqualTo("서울시 강남구 테헤란로 123");
        assertThat(response.getRestaurantDetail()).isEqualTo("강남역 3번 출구 근처");
        assertThat(response.getLat()).isEqualTo(37.498095);
        assertThat(response.getLnt()).isEqualTo(127.027610);

        // DB 데이터 검증
        Optional<StoreEntity> savedStore = storeRepository.findByRestaurantName("맛있는 식당");
        assertThat(savedStore).isPresent();
        assertThat(savedStore.get().getStoreId()).isEqualTo(response.getStoreId()); // DB 저장된 ID와 응답 ID 일치
        assertThat(savedStore.get().getManagerName()).isEqualTo("홍길동");
        assertThat(savedStore.get().getRestaurantName()).isEqualTo("맛있는 식당");
    }

    @Test
    @DisplayName("매장 등록 실패 - 중복된 가게 이름")
    void registerStore_Fail_DuplicateStoreName() {
        // Given: 이미 존재하는 가게 이름으로 매장 등록
        StoreRegister.Request request = new StoreRegister.Request(
                "김철수",
                "중복된 가게",
                "서울시 마포구 홍대입구 456",
                "홍대역 근처",
                37.555167,
                126.936991
        );

        // 먼저 매장을 등록
        storeService.registerStore(request);

        // 동일한 가게 이름으로 다시 등록하면 예외 발생해야 함
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.registerStore(request);
        });

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 가게 이름입니다.");
    }
}
