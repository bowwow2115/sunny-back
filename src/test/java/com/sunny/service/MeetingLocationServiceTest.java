package com.sunny.service;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.MeetingLocation;
import com.sunny.model.SunnyRide;
import com.sunny.model.dto.MeetingLocationDto;
import com.sunny.model.dto.SunnyRideDto;
import com.sunny.repository.MeetingLocationRepository;
import com.sunny.repository.SunnyRideRepository;
import com.sunny.service.impl.MeetingLocationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MeetingLocationService 단위테스트")
public class MeetingLocationServiceTest {

    @Mock
    private SunnyRideRepository sunnyRideRepository;

    @Mock
    private MeetingLocationRepository meetingLocationRepository;

    @InjectMocks
    private MeetingLocationServiceImpl meetingLocationService;

    // ==================== findAll ====================
    @Test
    @DisplayName("모든 승하차장소 조회 - Null 반환")
    void testFindAll_ReturnsNull() {
        // When
        java.util.List<MeetingLocationDto> result = meetingLocationService.findAll();

        // Then
        assertThat(result).isNull();
    }

    // ==================== findById ====================
    @Test
    @DisplayName("ID로 승하차장소 조회 - Null 반환")
    void testFindById_ReturnsNull() {
        // When
        MeetingLocationDto result = meetingLocationService.findById(1L);

        // Then
        assertThat(result).isNull();
    }

    // ==================== create ====================
    @Test
    @DisplayName("승하차장소 생성 - 성공")
    void testCreate_Success() {
        // Given
        Long rideId = 1L;
        SunnyRide ride = SunnyRide.builder()
                .id(rideId)
                .build();

        SunnyRideDto rideDto = SunnyRideDto.builder()
                .id(rideId)
                .build();

        MeetingLocationDto locationDto = MeetingLocationDto.builder()
                .name("강남역 1번출구")
                .time("08:30")
                .sunnyRide(rideDto)
                .build();

        MeetingLocation savedLocation = MeetingLocation.builder()
                .id(1L)
                .name("강남역 1번출구")
                .time("08:30")
                .sunnyRide(ride)
                .build();

        when(sunnyRideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(meetingLocationRepository.save(any(MeetingLocation.class))).thenReturn(savedLocation);

        // When
        MeetingLocationDto result = meetingLocationService.create(locationDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("강남역 1번출구");
        verify(sunnyRideRepository, times(1)).findById(rideId);
        verify(meetingLocationRepository, times(1)).save(any(MeetingLocation.class));
    }

    @Test
    @DisplayName("승하차장소 생성 - 차량 없음")
    void testCreate_RideNotFound() {
        // Given
        Long rideId = 999L;
        SunnyRideDto rideDto = SunnyRideDto.builder().id(rideId).build();
        MeetingLocationDto locationDto = MeetingLocationDto.builder()
                .name("강남역 1번출구")
                .sunnyRide(rideDto)
                .build();

        when(sunnyRideRepository.findById(rideId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> meetingLocationService.create(locationDto))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
        verify(sunnyRideRepository, times(1)).findById(rideId);
        verify(meetingLocationRepository, never()).save(any(MeetingLocation.class));
    }

    // ==================== update ====================
    @Test
    @DisplayName("승하차장소 업데이트 - 성공")
    void testUpdate_Success() {
        // Given
        Long locationId = 1L;
        SunnyRide ride = SunnyRide.builder()
                .id(1L)
                .build();

        MeetingLocationDto updateDto = MeetingLocationDto.builder()
                .id(locationId)
                .name("강남역 2번출구")
                .time("09:00")
                .build();

        MeetingLocation existingLocation = MeetingLocation.builder()
                .id(locationId)
                .name("강남역 1번출구")
                .time("08:30")
                .sunnyRide(ride)
                .build();

        MeetingLocation updatedLocation = MeetingLocation.builder()
                .id(locationId)
                .name("강남역 2번출구")
                .time("09:00")
                .sunnyRide(ride)
                .build();

        when(meetingLocationRepository.findById(locationId)).thenReturn(Optional.of(existingLocation));
        when(meetingLocationRepository.save(any(MeetingLocation.class))).thenReturn(updatedLocation);

        // When
        MeetingLocationDto result = meetingLocationService.update(updateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("강남역 2번출구");
        assertThat(result.getTime()).isEqualTo("09:00");
        verify(meetingLocationRepository, times(1)).findById(locationId);
        verify(meetingLocationRepository, times(1)).save(any(MeetingLocation.class));
    }

    @Test
    @DisplayName("승하차장소 업데이트 - 장소 없음")
    void testUpdate_LocationNotFound() {
        // Given
        Long locationId = 999L;
        MeetingLocationDto updateDto = MeetingLocationDto.builder()
                .id(locationId)
                .name("강남역 2번출구")
                .build();

        when(meetingLocationRepository.findById(locationId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> meetingLocationService.update(updateDto))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
    }

    // ==================== deleteById ====================
    @Test
    @DisplayName("승하차장소 삭제 - 성공")
    void testDeleteById_Success() {
        // Given
        Long locationId = 1L;

        // When
        meetingLocationService.deleteById(locationId);

        // Then
        verify(meetingLocationRepository, times(1)).deleteById(locationId);
    }

    // ==================== delete ====================
    @Test
    @DisplayName("승하차장소 삭제 - 성공 (delete)")
    void testDelete_Success() {
        // Given
        MeetingLocationDto locationDto = MeetingLocationDto.builder()
                .id(1L)
                .name("강남역 1번출구")
                .build();

        // When (delete는 아무 작업도 하지 않음)
        meetingLocationService.delete(locationDto);

        // Then
        // delete 메서드는 구현이 없음
        verify(meetingLocationRepository, never()).delete(any());
    }
}
