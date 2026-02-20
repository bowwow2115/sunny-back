package com.sunny.service;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.SunnyRide;
import com.sunny.model.dto.SunnyRideDto;
import com.sunny.repository.SunnyRideRepository;
import com.sunny.service.impl.SunnyRideServiceImpl;
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
@DisplayName("SunnyRideService 단위테스트")
public class SunnyRideServiceTest {

    @Mock
    private SunnyRideRepository sunnyRideRepository;

    @InjectMocks
    private SunnyRideServiceImpl sunnyRideService;

    // ==================== findAll ====================
    @Test
    @DisplayName("모든 차량 조회 - 성공")
    void testFindAll_Success() {
        // Given
        SunnyRide ride1 = SunnyRide.builder()
                .id(1L)
                .name("오전차량1")
                .isAm(true)
                .time("09:00")
                .build();

        SunnyRide ride2 = SunnyRide.builder()
                .id(2L)
                .name("오후차량1")
                .isAm(false)
                .time("16:00")
                .build();

        when(sunnyRideRepository.findAll()).thenReturn(List.of(ride1, ride2));

        // When
        List<SunnyRideDto> result = sunnyRideService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("오전차량1");
        assertThat(result.get(1).getName()).isEqualTo("오후차량1");
        verify(sunnyRideRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("모든 차량 조회 - 빈 리스트")
    void testFindAll_Empty() {
        // Given
        when(sunnyRideRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<SunnyRideDto> result = sunnyRideService.findAll();

        // Then
        assertThat(result).isEmpty();
        verify(sunnyRideRepository, times(1)).findAll();
    }

    // ==================== findById ====================
    @Test
    @DisplayName("ID로 차량 조회 - 성공")
    void testFindById_Success() {
        // Given
        Long rideId = 1L;
        SunnyRide ride = SunnyRide.builder()
                .id(1L)
                .name("오전차량1")
                .isAm(true)
                .time("09:00")
                .build();

        when(sunnyRideRepository.findById(rideId)).thenReturn(Optional.of(ride));

        // When
        SunnyRideDto result = sunnyRideService.findById(rideId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(rideId);
        verify(sunnyRideRepository, times(1)).findById(rideId);
    }

    @Test
    @DisplayName("ID로 차량 조회 - 차량 없음")
    void testFindById_RideNotFound() {
        // Given
        Long rideId = 999L;
        when(sunnyRideRepository.findById(rideId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> sunnyRideService.findById(rideId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
        verify(sunnyRideRepository, times(1)).findById(rideId);
    }

    // ==================== create ====================
    @Test
    @DisplayName("차량 생성 - 성공")
    void testCreate_Success() {
        // Given
        SunnyRideDto rideDto = SunnyRideDto.builder()
                .name("오전차량1")
                .isAm(true)
                .time("09:00")
                .build();

        SunnyRide savedRide = SunnyRide.builder()
                .id(1L)
                .name("오전차량1")
                .isAm(true)
                .time("09:00")
                .build();

        when(sunnyRideRepository.save(any(SunnyRide.class))).thenReturn(savedRide);

        // When
        SunnyRideDto result = sunnyRideService.create(rideDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("오전차량1");
        verify(sunnyRideRepository, times(1)).save(any(SunnyRide.class));
    }

    // ==================== update ====================
    @Test
    @DisplayName("차량 정보 업데이트 - 성공")
    void testUpdate_Success() {
        // Given
        Long rideId = 1L;
        SunnyRideDto updateDto = SunnyRideDto.builder()
                .id(rideId)
                .name("오전차량1_수정")
                .isAm(true)
                .time("09:00")
                .build();

        SunnyRide existingRide = SunnyRide.builder()
                .id(rideId)
                .name("오전차량1")
                .isAm(true)
                .time("09:00")
                .build();

        SunnyRide updatedRide = SunnyRide.builder()
                .id(rideId)
                .name("오전차량1_수정")
                .isAm(true)
                .time("09:00")
                .build();

        when(sunnyRideRepository.findById(rideId)).thenReturn(Optional.of(existingRide));
        when(sunnyRideRepository.save(any(SunnyRide.class))).thenReturn(updatedRide);

        // When
        SunnyRideDto result = sunnyRideService.update(updateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("오전차량1_수정");
        verify(sunnyRideRepository, times(1)).findById(rideId);
        verify(sunnyRideRepository, times(1)).save(any(SunnyRide.class));
    }

    @Test
    @DisplayName("차량 정보 업데이트 - 차량 없음")
    void testUpdate_RideNotFound() {
        // Given
        Long rideId = 999L;
        SunnyRideDto updateDto = SunnyRideDto.builder()
                .id(rideId)
                .build();

        when(sunnyRideRepository.findById(rideId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> sunnyRideService.update(updateDto))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
    }

    // ==================== delete ====================
    @Test
    @DisplayName("차량 삭제 - 성공 (delete)")
    void testDelete_Success() {
        // Given
        SunnyRideDto rideDto = SunnyRideDto.builder()
                .id(1L)
                .build();

        // When
        sunnyRideService.delete(rideDto);

        // Then
        verify(sunnyRideRepository, times(1)).delete(any(SunnyRide.class));
    }

    @Test
    @DisplayName("차량 삭제 - 성공 (deleteById)")
    void testDeleteById_Success() {
        // Given
        Long rideId = 1L;

        // When
        sunnyRideService.deleteById(rideId);

        // Then
        verify(sunnyRideRepository, times(1)).deleteById(rideId);
    }
}
