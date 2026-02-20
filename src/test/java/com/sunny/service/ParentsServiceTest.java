package com.sunny.service;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.Child;
import com.sunny.model.Parents;
import com.sunny.model.dto.ParentsDto;
import com.sunny.repository.ChildRepository;
import com.sunny.repository.ParentsRepository;
import com.sunny.service.impl.ParentsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ParentsService 단위테스트")
public class ParentsServiceTest {

    @Mock
    private ParentsRepository parentsRepository;

    @Mock
    private ChildRepository childRepository;

    @InjectMocks
    private ParentsServiceImpl parentsService;

    // ==================== findAll ====================
    @Test
    @DisplayName("모든 부모 조회 - Null 반환")
    void testFindAll_ReturnsNull() {
        // When
        java.util.List<ParentsDto> result = parentsService.findAll();

        // Then
        assertThat(result).isNull();
    }

    // ==================== findById ====================
    @Test
    @DisplayName("ID로 부모 정보 조회 - 성공")
    void testFindById_Success() {
        // Given
        Long parentsId = 1L;
        Parents parents = Parents.builder()
                .id(parentsId)
                .name("김부모")
                .telephone("010-1234-5678")
                .relation("아버지")
                .build();

        when(parentsRepository.findById(parentsId)).thenReturn(Optional.of(parents));

        // When
        ParentsDto result = parentsService.findById(parentsId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(parentsId);
        assertThat(result.getName()).isEqualTo("김부모");
        verify(parentsRepository, times(1)).findById(parentsId);
    }

    @Test
    @DisplayName("ID로 부모 정보 조회 - 부모 없음")
    void testFindById_ParentsNotFound() {
        // Given
        Long parentsId = 999L;
        when(parentsRepository.findById(parentsId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> parentsService.findById(parentsId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
        verify(parentsRepository, times(1)).findById(parentsId);
    }

    // ==================== create ====================
    @Test
    @DisplayName("부모 정보 생성 - 성공")
    void testCreate_Success() {
        // Given
        Long childId = 1L;
        Child child = Child.builder()
                .id(childId)
                .name("김어린이")
                .build();

        ParentsDto parentsDto = ParentsDto.builder()
                .name("김부모")
                .telephone("010-1234-5678")
                .relation("아버지")
                .childId(childId)
                .build();

        Parents createdParents = Parents.builder()
                .id(1L)
                .name("김부모")
                .telephone("010-1234-5678")
                .relation("아버지")
                .child(child)
                .build();

        when(childRepository.findById(childId)).thenReturn(Optional.of(child));
        when(parentsRepository.save(any(Parents.class))).thenReturn(createdParents);

        // When
        ParentsDto result = parentsService.create(parentsDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("김부모");
        verify(childRepository, times(1)).findById(childId);
        verify(parentsRepository, times(1)).save(any(Parents.class));
    }

    @Test
    @DisplayName("부모 정보 생성 - 원아 없음")
    void testCreate_ChildNotFound() {
        // Given
        Long childId = 999L;
        ParentsDto parentsDto = ParentsDto.builder()
                .name("김부모")
                .childId(childId)
                .build();

        when(childRepository.findById(childId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> parentsService.create(parentsDto))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
        verify(childRepository, times(1)).findById(childId);
        verify(parentsRepository, never()).save(any(Parents.class));
    }

    // ==================== update ====================
    @Test
    @DisplayName("부모 정보 업데이트 - 성공")
    void testUpdate_Success() {
        // Given
        ParentsDto updateDto = ParentsDto.builder()
                .id(1L)
                .name("김부모_수정")
                .telephone("010-9999-9999")
                .relation("어머니")
                .build();

        Parents updatedParents = Parents.builder()
                .id(1L)
                .name("김부모_수정")
                .telephone("010-9999-9999")
                .relation("어머니")
                .build();

        when(parentsRepository.save(any(Parents.class))).thenReturn(updatedParents);

        // When
        ParentsDto result = parentsService.update(updateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("김부모_수정");
        verify(parentsRepository, times(1)).save(any(Parents.class));
    }

    // ==================== delete ====================
    @Test
    @DisplayName("부모 정보 삭제 - 성공 (delete)")
    void testDelete_Success() {
        // Given
        ParentsDto parentsDto = ParentsDto.builder()
                .id(1L)
                .name("김부모")
                .build();

        // When
        parentsService.delete(parentsDto);

        // Then
        verify(parentsRepository, times(1)).delete(any(Parents.class));
    }

    @Test
    @DisplayName("부모 정보 삭제 - 성공 (deleteById)")
    void testDeleteById_Success() {
        // Given
        Long parentsId = 1L;

        // When
        parentsService.deleteById(parentsId);

        // Then
        verify(parentsRepository, times(1)).deleteById(parentsId);
    }

    // ==================== findByName ====================
    @Test
    @DisplayName("부모명으로 부모 정보 조회 - 성공")
    void testFindByName_Success() {
        // Given
        String parentName = "김부모";
        Parents parents = Parents.builder()
                .id(1L)
                .name(parentName)
                .telephone("010-1234-5678")
                .build();

        when(parentsRepository.findByName(parentName)).thenReturn(List.of(parents));

        // When
        List<ParentsDto> result = parentsService.findByName(parentName);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).extracting(ParentsDto::getName).containsOnly(parentName);
        verify(parentsRepository, times(1)).findByName(parentName);
    }

    @Test
    @DisplayName("부모명으로 부모 정보 조회 - 부모 없음")
    void testFindByName_ParentsNotFound() {
        // Given
        String parentName = "없는부모";
        when(parentsRepository.findByName(parentName)).thenReturn(null);

        // When
        List<ParentsDto> result = parentsService.findByName(parentName);

        // Then
        assertThat(result).size().isZero();
        verify(parentsRepository, times(1)).findByName(parentName);
    }
}
