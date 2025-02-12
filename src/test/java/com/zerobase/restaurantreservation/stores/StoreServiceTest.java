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

    private Integer savedStoreId;

    @BeforeEach
    void setUp() {
        storeRepository.deleteAll(); // 테스트 시작 전 DB 초기화
        // 매장 하나 등록
        StoreRegister.Request request = new StoreRegister.Request(
                "매니저1", "테스트 매장", "서울 강남구", "깔끔한 인테리어", 37.5665, 126.9780
        );
        StoreRegister.Response response = storeService.registerStore(request);
        savedStoreId = response.getStoreId();
    }

    @AfterEach
    void tearDown() {
        storeRepository.deleteAll(); // 테스트 종료 후 DB 정리
    }

    @Test
    @DisplayName("매장 등록 성공 - JPA 저장 확인")
    void registerStore_Success() {
        // Given
        StoreRegister.Request request = new StoreRegister.Request(
                "홍길동", "맛있는 식당", "서울시 강남구 테헤란로 123", "강남역 3번 출구 근처",
                37.498095, 127.027610
        );

        // When
        StoreRegister.Response response = storeService.registerStore(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStoreId()).isNotNull();
        assertThat(response.getRestaurantName()).isEqualTo("맛있는 식당");

        Optional<StoreEntity> savedStore = storeRepository.findByRestaurantName("맛있는 식당");
        assertThat(savedStore).isPresent();
    }

    @Test
    @DisplayName("매장 등록 실패 - 중복된 가게 이름")
    void registerStore_Fail_DuplicateStoreName() {
        // Given
        StoreRegister.Request request = new StoreRegister.Request(
                "김철수", "중복된 가게", "서울시 마포구 홍대입구 456", "홍대역 근처",
                37.555167, 126.936991
        );

        // 먼저 매장을 등록
        storeService.registerStore(request);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.registerStore(request);
        });

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 가게 이름입니다.");
    }

    @Test
    @DisplayName("매장 수정 성공 - JPA 저장 확인")
    void updateStore_Success() {
        // Given
        StoreRegister.Request updateRequest = new StoreRegister.Request(
                "매니저1", "수정된 매장", "서울시 서초구", "서초역 근처",
                37.4912, 127.0092
        );

        // When
        StoreRegister.Response updatedStore = storeService.updateStore(savedStoreId, updateRequest);

        // Then
        assertThat(updatedStore.getRestaurantName()).isEqualTo("수정된 매장");
        assertThat(updatedStore.getRestaurantAddress()).isEqualTo("서울시 서초구");
    }

    @Test
    @DisplayName("매장 삭제 성공")
    void deleteStore_Success() {
        // Given
        assertThat(storeRepository.findById(savedStoreId)).isPresent();

        // When
        storeService.deleteStore(savedStoreId);

        // Then
        Optional<StoreEntity> deletedStore = storeRepository.findById(savedStoreId);
        assertThat(deletedStore).isEmpty();
    }

    @Test
    @DisplayName("매장 삭제 실패 - 존재하지 않는 매장 ID")
    void deleteStore_Fail_StoreNotFound() {
        // Given
        Integer nonExistingStoreId = 9999;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.deleteStore(nonExistingStoreId);
        });

        assertThat(exception.getMessage()).isEqualTo("해당 매장이 존재하지 않습니다.");
    }
}
