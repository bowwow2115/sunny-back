package com.sunny.service;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.SunnyClass;
import com.sunny.model.dto.SunnyClassDto;
import com.sunny.repository.SunnyClassRepository;
import com.sunny.service.impl.SunnyClsssServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SunnyClassService 단위테스트")
public class SunnyClassServiceTest {

    @Mock
    private SunnyClassRepository sunnyClassRepository;

    @InjectMocks
    private SunnyClsssServiceImpl sunnyClassService;

    // ==================== findAll ====================
    @Test
    @DisplayName("모든 반 조회 - 성공")
    void testFindAll_Success() {
        // Given
        SunnyClass class1 = SunnyClass.builder()
                .id(1L)
                .name("반1")
                .build();

        SunnyClass class2 = SunnyClass.builder()
                .id(2L)
                .name("반2")
                .build();

        when(sunnyClassRepository.findAll()).thenReturn(List.of(class1, class2));

        // When
        List<SunnyClassDto> result = sunnyClassService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("반1");
        assertThat(result.get(1).getName()).isEqualTo("반2");
        verify(sunnyClassRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("모든 반 조회 - 빈 리스트")
    void testFindAll_Empty() {
        // Given
        when(sunnyClassRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<SunnyClassDto> result = sunnyClassService.findAll();

        // Then
        assertThat(result).isEmpty();
        verify(sunnyClassRepository, times(1)).findAll();
    }

    // ==================== findById ====================
    @Test
    @DisplayName("ID로 반 조회 - 성공")
    void testFindById_Success() {
        // Given
        Long classId = 1L;
        SunnyClass sunnyClass = SunnyClass.builder()
                .id(classId)
                .name("반1")
                .build();

        when(sunnyClassRepository.findById(classId)).thenReturn(Optional.of(sunnyClass));

        // When
        SunnyClassDto result = sunnyClassService.findById(classId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(classId);
        assertThat(result.getName()).isEqualTo("반1");
        verify(sunnyClassRepository, times(1)).findById(classId);
    }

    @Test
    @DisplayName("ID로 반 조회 - 반 없음")
    void testFindById_ClassNotFound() {
        // Given
        Long classId = 999L;
        when(sunnyClassRepository.findById(classId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> sunnyClassService.findById(classId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
        verify(sunnyClassRepository, times(1)).findById(classId);
    }

    // ==================== create ====================
    @Test
    @DisplayName("반 생성 - 성공")
    void testCreate_Success() {
        // Given
        SunnyClassDto classDto = SunnyClassDto.builder()
                .name("반3")
                .build();

        SunnyClass savedClass = SunnyClass.builder()
                .id(3L)
                .name("반3")
                .build();

        when(sunnyClassRepository.save(any(SunnyClass.class))).thenReturn(savedClass);

        // When
        SunnyClassDto result = sunnyClassService.create(classDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("반3");
        verify(sunnyClassRepository, times(1)).save(any(SunnyClass.class));
    }
//
//    // ==================== update ====================
//    @Test
//    @DisplayName("반 정보 업데이트 - 성공")
//    void testUpdate_Success() {
//        // Given
//        Long classId = 1L;
//        SunnyClassDto updateDto = SunnyClassDto.builder()
//                .id(classId)
//                .name("반1")
//                .build();
//
//        SunnyClass updatedClass = SunnyClass.builder()
//                .id(classId)
//                .name("반1")
//                .build();
//
//        when(sunnyClassRepository.save(any(SunnyClass.class))).thenReturn(updatedClass);
//
//        // When
//        SunnyClassDto result = sunnyClassService.update(updateDto);
//
//        // Then
//        assertThat(result).isNotNull();
//        verify(sunnyClassRepository, times(1)).save(any(SunnyClass.class));
//    }

    // ==================== delete ====================
    @Test
    @DisplayName("반 삭제 - 성공 (delete)")
    void testDelete_Success() {
        // Given
        SunnyClassDto classDto = SunnyClassDto.builder()
                .id(1L)
                .name("반1")
                .build();

        // When
        sunnyClassService.delete(classDto);

        // Then
        verify(sunnyClassRepository, times(1)).delete(any(SunnyClass.class));
    }

    @Test
    @DisplayName("반 삭제 - 성공 (deleteById)")
    void testDeleteById_Success() {
        // Given
        Long classId = 1L;

        // When
        sunnyClassService.deleteById(classId);

        // Then
        verify(sunnyClassRepository, times(1)).deleteById(classId);
    }
}
