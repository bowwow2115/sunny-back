package com.sunny.service;

import com.sunny.model.dto.ChildDto;
import com.sunny.service.impl.ExcelChildServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExcelChildService 단위테스트")
public class ExcelChildServiceTest {

    @Mock
    private ChildService childService;

    @InjectMocks
    private ExcelChildServiceImpl excelChildService;

    // ==================== createList ====================
    @Test
    @DisplayName("원아 목록 대량 생성 - 성공")
    void testCreateList_Success() {
        // Given
        ChildDto child1Dto = ChildDto.builder()
                .name("김어린이")
                .className("반1")
                .birthday(LocalDate.of(2020, 5, 15))
                .build();

        ChildDto child2Dto = ChildDto.builder()
                .name("이어린이")
                .className("반1")
                .birthday(LocalDate.of(2020, 6, 20))
                .build();

        ChildDto child3Dto = ChildDto.builder()
                .name("박어린이")
                .className("반2")
                .birthday(LocalDate.of(2020, 7, 10))
                .build();

        ChildDto savedChild1 = ChildDto.builder()
                .id(1L)
                .name("김어린이")
                .className("반1")
                .build();

        ChildDto savedChild2 = ChildDto.builder()
                .id(2L)
                .name("이어린이")
                .className("반1")
                .build();

        ChildDto savedChild3 = ChildDto.builder()
                .id(3L)
                .name("박어린이")
                .className("반2")
                .build();

        List<ChildDto> inputList = List.of(child1Dto, child2Dto, child3Dto);

        when(childService.create(child1Dto)).thenReturn(savedChild1);
        when(childService.create(child2Dto)).thenReturn(savedChild2);
        when(childService.create(child3Dto)).thenReturn(savedChild3);

        // When
        List<ChildDto> result = excelChildService.createList(inputList);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("김어린이");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(2).getId()).isEqualTo(3L);
        verify(childService, times(3)).create(any(ChildDto.class));
    }

    @Test
    @DisplayName("원아 목록 대량 생성 - 빈 리스트")
    void testCreateList_EmptyList() {
        // Given
        List<ChildDto> emptyList = new ArrayList<>();

        // When
        List<ChildDto> result = excelChildService.createList(emptyList);

        // Then
        assertThat(result).isEmpty();
        verify(childService, never()).create(any(ChildDto.class));
    }

    @Test
    @DisplayName("원아 목록 대량 생성 - 단일 항목")
    void testCreateList_SingleItem() {
        // Given
        ChildDto childDto = ChildDto.builder()
                .name("김어린이")
                .className("반1")
                .build();

        ChildDto savedChild = ChildDto.builder()
                .id(1L)
                .name("김어린이")
                .className("반1")
                .build();

        when(childService.create(childDto)).thenReturn(savedChild);

        // When
        List<ChildDto> result = excelChildService.createList(List.of(childDto));

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(childService, times(1)).create(childDto);
    }

    @Test
    @DisplayName("원아 목록 대량 생성 - 생성 순서 보장")
    void testCreateList_PreservesOrder() {
        // Given
        ChildDto child1 = ChildDto.builder().name("첫번째").build();
        ChildDto child2 = ChildDto.builder().name("두번째").build();
        ChildDto child3 = ChildDto.builder().name("세번째").build();

        ChildDto saved1 = ChildDto.builder().id(1L).name("첫번째").build();
        ChildDto saved2 = ChildDto.builder().id(2L).name("두번째").build();
        ChildDto saved3 = ChildDto.builder().id(3L).name("세번째").build();

        List<ChildDto> inputList = List.of(child1, child2, child3);

        when(childService.create(child1)).thenReturn(saved1);
        when(childService.create(child2)).thenReturn(saved2);
        when(childService.create(child3)).thenReturn(saved3);

        // When
        List<ChildDto> result = excelChildService.createList(inputList);

        // Then
        assertThat(result).extracting("name").containsExactly("첫번째", "두번째", "세번째");
        verify(childService).create(child1);
        verify(childService).create(child2);
        verify(childService).create(child3);
    }

    @Test
    @DisplayName("원아 목록 대량 생성 - 일부 생성 실패 시 예외")
    void testCreateList_PartialFailure() {
        // Given
        ChildDto child1 = ChildDto.builder().name("첫번째").build();
        ChildDto child2 = ChildDto.builder().name("두번째").build();

        ChildDto saved1 = ChildDto.builder().id(1L).name("첫번째").build();

        List<ChildDto> inputList = List.of(child1, child2);

        when(childService.create(child1)).thenReturn(saved1);
        when(childService.create(child2)).thenThrow(new RuntimeException("생성 실패"));

        // When & Then
        assertThatThrownBy(() -> excelChildService.createList(inputList))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("생성 실패");
    }

    // ==================== findAll ====================
    @Test
    @DisplayName("모든 원아 조회 - Null 반환")
    void testFindAll_ReturnsNull() {
        // When
        List<ChildDto> result = excelChildService.findAll();

        // Then
        assertThat(result).isNull();
    }

    // ==================== findById ====================
    @Test
    @DisplayName("ID로 원아 조회 - Null 반환")
    void testFindById_ReturnsNull() {
        // When
        ChildDto result = excelChildService.findById(1L);

        // Then
        assertThat(result).isNull();
    }

    // ==================== create ====================
    @Test
    @DisplayName("원아 생성 - Null 반환")
    void testCreate_ReturnsNull() {
        // Given
        ChildDto childDto = ChildDto.builder()
                .name("김어린이")
                .build();

        // When
        ChildDto result = excelChildService.create(childDto);

        // Then
        assertThat(result).isNull();
    }

    // ==================== update ====================
    @Test
    @DisplayName("원아 업데이트 - Null 반환")
    void testUpdate_ReturnsNull() {
        // Given
        ChildDto childDto = ChildDto.builder()
                .id(1L)
                .name("김어린이_수정")
                .build();

        // When
        ChildDto result = excelChildService.update(childDto);

        // Then
        assertThat(result).isNull();
    }

    // ==================== delete ====================
    @Test
    @DisplayName("원아 삭제 - 성공 (delete)")
    void testDelete_Success() {
        // Given
        ChildDto childDto = ChildDto.builder()
                .id(1L)
                .name("김어린이")
                .build();

        // When
        excelChildService.delete(childDto);

        // Then
        // delete 메서드는 구현이 없음
    }

    @Test
    @DisplayName("원아 삭제 - 성공 (deleteById)")
    void testDeleteById_Success() {
        // Given
        Long childId = 1L;

        // When
        excelChildService.deleteById(childId);

        // Then
        // deleteById 메서드는 구현이 없음
    }
}
