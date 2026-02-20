package com.sunny.service;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.Child;
import com.sunny.model.ChildMeetingLocation;
import com.sunny.model.MeetingLocation;
import com.sunny.model.SunnyRide;
import com.sunny.model.dto.ChilMeetingLocationDto;
import com.sunny.model.dto.ChildDto;
import com.sunny.model.dto.MeetingLocationDto;
import com.sunny.repository.ChildMeetingLocationRepository;
import com.sunny.repository.ChildRepository;
import com.sunny.repository.MeetingLocationRepository;
import com.sunny.service.impl.ChildMeetingLocationServiceImpl;
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
@DisplayName("ChildMeetingLocationService 단위테스트")
public class ChildMeetingLocationServiceTest {

    @Mock
    private ChildMeetingLocationRepository childMeetingLocationRepository;

    @Mock
    private ChildRepository childRepository;

    @Mock
    private MeetingLocationRepository meetingLocationRepository;

    @InjectMocks
    private ChildMeetingLocationServiceImpl childMeetingLocationService;

    // ==================== findAll ====================
    @Test
    @DisplayName("모든 원아-승하차장소 매핑 조회 - Null 반환")
    void testFindAll_ReturnsNull() {
        // When
        java.util.List<ChilMeetingLocationDto> result = childMeetingLocationService.findAll();

        // Then
        assertThat(result).isNull();
    }

    // ==================== findById ====================
    @Test
    @DisplayName("ID로 원아-승하차장소 매핑 조회 - Null 반환")
    void testFindById_ReturnsNull() {
        // When
        ChilMeetingLocationDto result = childMeetingLocationService.findById(1L);

        // Then
        assertThat(result).isNull();
    }

    // ==================== create ====================
    @Test
    @DisplayName("원아-승하차장소 매핑 생성 - 성공")
    void testCreate_Success() {
        // Given
        Long childId = 1L;
        Long locationId = 1L;

        Child child = Child.builder()
                .id(childId)
                .name("김어린이")
                .className("반1")
                .build();

        SunnyRide ride = SunnyRide.builder()
                .id(1L)
                .name("오전 1차량")
                .time("09:00")
                .isAm(true)
                .build();

        MeetingLocation location = MeetingLocation.builder()
                .id(locationId)
                .name("공항초 정문 앞")
                .time("09:30")
                .sunnyRide(ride)
                .build();

        ChildMeetingLocation savedMapping = ChildMeetingLocation.builder()
                .id(1L)
                .child(child)
                .meetingLocation(location)
                .comment("안내자 필요")
                .build();

        ChildDto childDto = ChildDto.builder().id(childId).name("김어린이").build();
        MeetingLocationDto locationDto = MeetingLocationDto.builder().id(locationId).name("강남역 1번출구").build();
        ChilMeetingLocationDto mappingDto = ChilMeetingLocationDto.builder()
                .child(childDto)
                .meetingLocation(locationDto)
                .comment("안내자 필요")
                .build();

        when(childRepository.findById(childId)).thenReturn(Optional.of(child));
        when(meetingLocationRepository.findById(locationId)).thenReturn(Optional.of(location));
        when(childMeetingLocationRepository.save(any(ChildMeetingLocation.class))).thenReturn(savedMapping);

        // When
        ChilMeetingLocationDto result = childMeetingLocationService.create(mappingDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getComment()).isEqualTo("안내자 필요");
        assertThat(result.getChild().getName()).isEqualTo("김어린이");
        verify(childRepository, times(1)).findById(childId);
        verify(meetingLocationRepository, times(1)).findById(locationId);
        verify(childMeetingLocationRepository, times(1)).save(any(ChildMeetingLocation.class));
    }

    @Test
    @DisplayName("원아-승하차장소 매핑 생성 - 원아 없음")
    void testCreate_ChildNotFound() {
        // Given
        Long childId = 999L;
        Long locationId = 1L;

        ChildDto childDto = ChildDto.builder().id(childId).build();
        MeetingLocationDto locationDto = MeetingLocationDto.builder().id(locationId).build();
        ChilMeetingLocationDto mappingDto = ChilMeetingLocationDto.builder()
                .child(childDto)
                .meetingLocation(locationDto)
                .build();

        when(childRepository.findById(childId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> childMeetingLocationService.create(mappingDto))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
        verify(childRepository, times(1)).findById(childId);
        verify(meetingLocationRepository, never()).findById(any());
    }

    @Test
    @DisplayName("원아-승하차장소 매핑 생성 - 승하차장소 없음")
    void testCreate_LocationNotFound() {
        // Given
        Long childId = 1L;
        Long locationId = 999L;

        Child child = Child.builder()
                .id(childId)
                .name("김어린이")
                .build();

        ChildDto childDto = ChildDto.builder().id(childId).build();
        MeetingLocationDto locationDto = MeetingLocationDto.builder().id(locationId).build();
        ChilMeetingLocationDto mappingDto = ChilMeetingLocationDto.builder()
                .child(childDto)
                .meetingLocation(locationDto)
                .build();

        when(childRepository.findById(childId)).thenReturn(Optional.of(child));
        when(meetingLocationRepository.findById(locationId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> childMeetingLocationService.create(mappingDto))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
        verify(childRepository, times(1)).findById(childId);
        verify(meetingLocationRepository, times(1)).findById(locationId);
    }

    // ==================== update ====================
    @Test
    @DisplayName("원아-승하차장소 매핑 업데이트 - 성공")
    void testUpdate_Success() {
        // Given
        Long childId = 1L;
        Long locationId = 1L;

        Child child = Child.builder()
                .id(childId)
                .name("김어린이")
                .build();

        SunnyRide ride = SunnyRide.builder()
                .id(1L)
                .name("오전 1치량")
                .time("09:00")
                .build();

        MeetingLocation location = MeetingLocation.builder()
                .id(locationId)
                .name("강남역 1번출구")
                .time("09:00")
                .sunnyRide(ride)
                .build();

        ChildMeetingLocation updatedMapping = ChildMeetingLocation.builder()
                .id(1L)
                .child(child)
                .meetingLocation(location)
                .comment("보호자 동반")
                .build();

        ChildDto childDto = ChildDto.builder().id(childId).build();
        MeetingLocationDto locationDto = MeetingLocationDto.builder().id(locationId).build();
        ChilMeetingLocationDto updateDto = ChilMeetingLocationDto.builder()
                .id(1L)
                .child(childDto)
                .meetingLocation(locationDto)
                .comment("보호자 동반")
                .build();

        when(childRepository.findById(childId)).thenReturn(Optional.of(child));
        when(meetingLocationRepository.findById(locationId)).thenReturn(Optional.of(location));
        when(childMeetingLocationRepository.save(any(ChildMeetingLocation.class))).thenReturn(updatedMapping);

        // When
        ChilMeetingLocationDto result = childMeetingLocationService.update(updateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getComment()).isEqualTo("보호자 동반");
        verify(childRepository, times(1)).findById(childId);
        verify(meetingLocationRepository, times(1)).findById(locationId);
        verify(childMeetingLocationRepository, times(1)).save(any(ChildMeetingLocation.class));
    }

    @Test
    @DisplayName("원아-승하차장소 매핑 업데이트 - 원아 없음")
    void testUpdate_ChildNotFound() {
        // Given
        Long childId = 999L;
        Long locationId = 1L;

        ChildDto childDto = ChildDto.builder().id(childId).build();
        MeetingLocationDto locationDto = MeetingLocationDto.builder().id(locationId).build();
        ChilMeetingLocationDto updateDto = ChilMeetingLocationDto.builder()
                .id(1L)
                .child(childDto)
                .meetingLocation(locationDto)
                .build();

        when(childRepository.findById(childId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> childMeetingLocationService.update(updateDto))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
    }

    @Test
    @DisplayName("원아-승하차장소 매핑 업데이트 - 승하차장소 없음")
    void testUpdate_LocationNotFound() {
        // Given
        Long childId = 1L;
        Long locationId = 999L;

        Child child = Child.builder()
                .id(childId)
                .name("김어린이")
                .build();

        ChildDto childDto = ChildDto.builder().id(childId).build();
        MeetingLocationDto locationDto = MeetingLocationDto.builder().id(locationId).build();
        ChilMeetingLocationDto updateDto = ChilMeetingLocationDto.builder()
                .id(1L)
                .child(childDto)
                .meetingLocation(locationDto)
                .build();

        when(childRepository.findById(childId)).thenReturn(Optional.of(child));
        when(meetingLocationRepository.findById(locationId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> childMeetingLocationService.update(updateDto))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
    }

    // ==================== delete ====================
    @Test
    @DisplayName("원아-승하차장소 매핑 삭제 - 성공 (delete)")
    void testDelete_Success() {
        // Given
        ChildDto childDto = ChildDto.builder().id(1L).build();
        MeetingLocationDto locationDto = MeetingLocationDto.builder().id(1L).build();
        ChilMeetingLocationDto mappingDto = ChilMeetingLocationDto.builder()
                .id(1L)
                .child(childDto)
                .meetingLocation(locationDto)
                .build();

        // When (delete는 아무 작업도 하지 않음)
        childMeetingLocationService.delete(mappingDto);

        // Then
        // delete 메서드는 구현이 없음
        verify(childMeetingLocationRepository, never()).delete(any());
    }

    @Test
    @DisplayName("원아-승하차장소 매핑 삭제 - 성공 (deleteById)")
    void testDeleteById_Success() {
        // Given
        Long mappingId = 1L;

        // When
        childMeetingLocationService.deleteById(mappingId);

        // Then
        // 기본 CrudService 구현이 없으므로 메서드 호출만 확인
    }
}
