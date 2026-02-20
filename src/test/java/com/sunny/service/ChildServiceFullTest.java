package com.sunny.service;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.Child;
import com.sunny.model.dto.ChildDto;
import com.sunny.model.dto.ParentsDto;
import com.sunny.repository.ChildRepository;
import com.sunny.repository.MeetingLocationRepository;
import com.sunny.service.impl.ChildServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ChildService 단위테스트
 * JPA 환경에서는 insert, update시에 반환하는 데이터를 DB의 데이터와 동일하다고 사용
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ChildService 단위테스트")
public class ChildServiceFullTest {

    @Mock
    private ChildRepository childRepository;
    @Mock
    private MeetingLocationRepository meetingLocationRepository;
    @InjectMocks
    private ChildServiceImpl childService;

    private Random random = new Random();
    LocalDate admissionDate = generateRandomDate(2021, 2024);
    LocalDate birthday = generateRandomDate(2017, 2020);

    // ==================== findByName ====================
    @Test
    @DisplayName("원아 이름으로 조회 - 성공")
    void testFindByName_Success() {
        // Given
        String name = "박어린이";
        Child child = Child.builder()
                .id(1L)
                .admissionDate(admissionDate)
                .birthday(birthday)
                .name(name)
                .className("반1")
                .build();

        when(childRepository.findByName(name)).thenReturn(List.of(child));

        // When
        List<ChildDto> result = childService.findByName(name);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo(name);
        verify(childRepository, times(1)).findByName(name);
    }

    @Test
    @DisplayName("원아 이름으로 조회 - 결과 없음")
    void testFindByName_Empty() {
        // Given
        String name = "없는원아";
        when(childRepository.findByName(name)).thenReturn(new ArrayList<>());

        // When
        List<ChildDto> result = childService.findByName(name);

        // Then
        assertThat(result).isEmpty();
        verify(childRepository, times(1)).findByName(name);
    }

    // ==================== findChildWithBirthMonth ====================
    @Test
    @DisplayName("생월별 원아 조회 - 성공")
    void testFindChildWithBirthMonth_Success() {
        // Given
        int month = 3;
        Child child1 = Child.builder()
                .id(1L)
                .name("김어린이")
                .birthday(LocalDate.of(2020, 3, 15))
                .admissionDate(admissionDate)
                .birthday(birthday)
                .build();
        Child child2 = Child.builder()
                .id(2L)
                .name("이어린이")
                .admissionDate(admissionDate)
                .birthday(birthday)
                .birthday(LocalDate.of(2019, 3, 20))
                .build();

        when(childRepository.findChildWithBirthMonth(month)).thenReturn(List.of(child1, child2));

        // When
        List<ChildDto> result = childService.findChildWithBirthMonth(month);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("김어린이");
        assertThat(result.get(1).getName()).isEqualTo("이어린이");
        verify(childRepository, times(1)).findChildWithBirthMonth(month);
    }

    @Test
    @DisplayName("생월별 원아 조회 - 결과 없음")
    void testFindChildWithBirthMonth_Empty() {
        // Given
        int month = 12;
        when(childRepository.findChildWithBirthMonth(month)).thenReturn(new ArrayList<>());

        // When
        List<ChildDto> result = childService.findChildWithBirthMonth(month);

        // Then
        assertThat(result).isEmpty();
        verify(childRepository, times(1)).findChildWithBirthMonth(month);
    }

    // ==================== checkChild ====================
    @Test
    @DisplayName("원아 정보 확인 - 성공")
    void testCheckChild_Success() {
        // Given
        ChildDto checkDto = ChildDto.builder()
                .name("박어린이")
                .className("반1")
                .build();

        Child child = Child.builder()
                .id(1L)
                .name("박어린이")
                .className("반1")
                .build();

        when(childRepository.checkChild(any(Child.class))).thenReturn(List.of(child));

        // When
        List<ChildDto> result = childService.checkChild(checkDto);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("박어린이");
        verify(childRepository, times(1)).checkChild(any(Child.class));
    }

    @Test
    @DisplayName("원아 정보 확인 - 결과 없음")
    void testCheckChild_Empty() {
        // Given
        ChildDto checkDto = ChildDto.builder()
                .name("없는원아")
                .build();

        when(childRepository.checkChild(any(Child.class))).thenReturn(new ArrayList<>());

        // When
        List<ChildDto> result = childService.checkChild(checkDto);

        // Then
        assertThat(result).isEmpty();
        verify(childRepository, times(1)).checkChild(any(Child.class));
    }

    // ==================== getAttendingChildren ====================
    @Test
    @DisplayName("출석 원아 조회 - 성공")
    void testGetAttendingChildren_Success() {
        // Given
        Child child1 = Child.builder().id(1L).name("김어린이").className("반1").build();
        Child child2 = Child.builder().id(2L).name("이어린이").className("반1").build();

        when(childRepository.findAttendingChildren()).thenReturn(List.of(child1, child2));

        // When
        List<ChildDto> result = childService.getAttendingChildren();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("김어린이");
        verify(childRepository, times(1)).findAttendingChildren();
    }

    @Test
    @DisplayName("출석 원아 조회 - 결과 없음")
    void testGetAttendingChildren_Empty() {
        // Given
        when(childRepository.findAttendingChildren()).thenReturn(new ArrayList<>());

        // When
        List<ChildDto> result = childService.getAttendingChildren();

        // Then
        assertThat(result).isEmpty();
        verify(childRepository, times(1)).findAttendingChildren();
    }

    // ==================== findAll ====================
    @Test
    @DisplayName("모든 원아 조회 - 성공")
    void testFindAll_Success() {
        // Given
        Child child1 = Child.builder().id(1L).name("김어린이").className("반1").build();
        Child child2 = Child.builder().id(2L).name("이어린이").className("반2").build();

        when(childRepository.findAll()).thenReturn(List.of(child1, child2));

        // When
        List<ChildDto> result = childService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("김어린이");
        assertThat(result.get(1).getName()).isEqualTo("이어린이");
        verify(childRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("모든 원아 조회 - 빈 리스트")
    void testFindAll_Empty() {
        // Given
        when(childRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<ChildDto> result = childService.findAll();

        // Then
        assertThat(result).isEmpty();
        verify(childRepository, times(1)).findAll();
    }

    // ==================== findById ====================
    @Test
    @DisplayName("ID로 원아 조회 - 성공")
    void testFindById_Success() {
        // Given
        Long childId = 1L;
        Child child = Child.builder()
                .id(childId)
                .name("김어린이")
                .className("반1")
                .birthday(LocalDate.of(2020, 5, 15))
                .build();

        when(childRepository.findById(childId)).thenReturn(Optional.of(child));

        // When
        ChildDto result = childService.findById(childId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(childId);
        assertThat(result.getName()).isEqualTo("김어린이");
        verify(childRepository, times(1)).findById(childId);
    }

    @Test
    @DisplayName("ID로 원아 조회 - 원아 없음")
    void testFindById_ChildNotFound() {
        // Given
        Long childId = 999L;
        when(childRepository.findById(childId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> childService.findById(childId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
        verify(childRepository, times(1)).findById(childId);
    }

    // ==================== create ====================
    @Test
    @DisplayName("원아 생성 - 성공")
    void testCreate_Success() {
        // Given
        ChildDto childDto = ChildDto.builder()
                .name("박새로운")
                .className("반1")
                .birthday(LocalDate.of(2021, 6, 10))
                .parentList(new ArrayList<>())
                .childRideList(new ArrayList<>())
                .build();

        Child savedChild = Child.builder()
                .id(1L)
                .name("박새로운")
                .className("반1")
                .birthday(LocalDate.of(2021, 6, 10))
                .build();

        when(childRepository.save(any(Child.class))).thenReturn(savedChild);

        // When
        ChildDto result = childService.create(childDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("박새로운");
        verify(childRepository, times(1)).save(any(Child.class));
    }

    @Test
    @DisplayName("원아 생성 - 부모 정보 포함")
    void testCreate_WithParents() {
        // Given
        ParentsDto parentsDto = ParentsDto.builder()
                .id(1L)
                .name("김부모")
                .relation("아버지")
                .build();

        ChildDto childDto = ChildDto.builder()
                .name("박새로운")
                .className("반1")
                .parentList(List.of(parentsDto))
                .childRideList(new ArrayList<>())
                .build();

        Child savedChild = Child.builder()
                .id(1L)
                .name("박새로운")
                .className("반1")
                .build();

        when(childRepository.save(any(Child.class))).thenReturn(savedChild);

        // When
        ChildDto result = childService.create(childDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(childRepository, times(1)).save(any(Child.class));
    }

    // ==================== update ====================
    @Test
    @DisplayName("원아 정보 업데이트 - 성공")
    void testUpdate_Success() {
        // Given
        LocalDate admissionDate = generateRandomDate(2021, 2024);
        LocalDate birthday = generateRandomDate(2017, 2020);

        Long childId = 1L;
        ChildDto updateDto = ChildDto.builder()
                .id(childId)
                .birthday(birthday)
                .className("반2")
                .build();

        Child existingChild = Child.builder()
                .id(childId)
                .name("김어린이")
                .className("반1")
                .admissionDate(admissionDate)
                .birthday(birthday)
                .build();

        Child updatedChild = Child.builder()
                .id(childId)
                .name("김어린이_수정")
                .className("반2")
                .build();

        when(childRepository.findById(childId)).thenReturn(Optional.of(existingChild));
        when(childRepository.save(any(Child.class))).thenReturn(updatedChild);

        // When
        ChildDto result = childService.update(updateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("김어린이_수정");
        assertThat(result.getClassName()).isEqualTo("반2");
        verify(childRepository, times(1)).findById(childId);
        verify(childRepository, times(1)).save(any(Child.class));
    }

    // ==================== updateChildrenClass ====================
    @Test
    @DisplayName("여러 원아의 클래스 업데이트 - 성공")
    void testUpdateChildrenClass_Success() {
        // Given
        Long childId1 = 1L;
        Long childId2 = 2L;
        String newClassName = "반3";

        ChildDto childDto1 = ChildDto.builder().id(childId1).name("김어린이").build();
        ChildDto childDto2 = ChildDto.builder().id(childId2).name("이어린이").build();

        Child child1 = Child.builder().id(childId1).name("김어린이").className("반1").build();
        Child child2 = Child.builder().id(childId2).name("이어린이").className("반2").build();

        Child updatedChild1 = Child.builder().id(childId1).name("김어린이").className(newClassName).build();
        Child updatedChild2 = Child.builder().id(childId2).name("이어린이").className(newClassName).build();

        when(childRepository.findById(childId1)).thenReturn(Optional.of(child1));
        when(childRepository.findById(childId2)).thenReturn(Optional.of(child2));
        when(childRepository.save(any(Child.class)))
                .thenReturn(updatedChild1)
                .thenReturn(updatedChild2);

        // When
        List<ChildDto> result = childService.updateChildrenClass(
                List.of(childDto1, childDto2), newClassName);

        // Then
        assertThat(result).hasSize(2);
        verify(childRepository, times(2)).findById(any());
        verify(childRepository, times(2)).save(any(Child.class));
    }

    @Test
    @DisplayName("여러 원아의 클래스 업데이트 - 원아 없음")
    void testUpdateChildrenClass_ChildNotFound() {
        // Given
        Long childId = 999L;
        ChildDto childDto = ChildDto.builder().id(childId).name("없는원아").build();

        when(childRepository.findById(childId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> childService.updateChildrenClass(
                List.of(childDto), "반3"))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ENTITY_NOT_FOUND);
    }

    // ==================== findAllWithRide ====================
    @Test
    @DisplayName("차량 정보 포함한 모든 원아 조회 - 성공")
    void testFindAllWithRide_Success() {
        // Given
        LocalDate admissionDate = generateRandomDate(2021, 2024);
        LocalDate birthday = generateRandomDate(2017, 2020);

        Child child = Child.builder()
                .id(1L)
                .name("김어린이")
                .className("반1")
                .admissionDate(admissionDate)
                .birthday(birthday)
                .build();

        when(childRepository.findAllWithRide()).thenReturn(List.of(child));

        // When
        List<ChildDto> result = childService.findAllWithRide();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("김어린이");
        verify(childRepository, times(1)).findAllWithRide();
    }

    @Test
    @DisplayName("차량 정보 포함한 모든 원아 조회 - 빈 리스트")
    void testFindAllWithRide_Empty() {
        // Given
        when(childRepository.findAllWithRide()).thenReturn(new ArrayList<>());

        // When
        List<ChildDto> result = childService.findAllWithRide();

        // Then
        assertThat(result).isEmpty();
        verify(childRepository, times(1)).findAllWithRide();
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
        childService.delete(childDto);

        // Then
        verify(childRepository, times(1)).delete(any(Child.class));
    }

    @Test
    @DisplayName("원아 삭제 - 성공 (deleteById)")
    void testDeleteById_Success() {
        // Given
        Long childId = 1L;

        // When
        childService.deleteById(childId);

        // Then
        verify(childRepository, times(1)).deleteById(childId);
    }

    private LocalDate generateRandomDate(int startYear, int endYear) {
        int year = random.nextInt(endYear - startYear + 1) + startYear;
        int dayOfYear = random.nextInt(365) + 1;
        return LocalDate.ofYearDay(year, dayOfYear);
    }
}
