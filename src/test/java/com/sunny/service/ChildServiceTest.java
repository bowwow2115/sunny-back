package com.sunny.service;

import com.sunny.code.SunnyCode;
import com.sunny.model.Child;
import com.sunny.model.dto.ChildDto;
import com.sunny.repository.ChildRepository;
import com.sunny.repository.MeetingLocationRepository;
import com.sunny.service.impl.ChildServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JPA 환경에서는 inesrt, update시에 반환하는 데이터를 DB의 데이터와 동일하다고 사용
 * Mybatis 환경에서는 새로 select 해와야 함
 */
@ExtendWith(MockitoExtension.class)
public class ChildServiceTest {
    @Mock
    private ChildRepository mockChildRepository;
    @Mock
    private MeetingLocationRepository mockMeetingLocationRepository;
    @InjectMocks
    private ChildServiceImpl mockChildService;

    @Test
    @DisplayName("원아 이름으로 가져오기 테스트")
    public void findByNameMock() {
        Child mockChild = Child.builder()
                .id(1L)
                .name("박어린이")
                .build();
        List<Child> mockChildList = new ArrayList<>();
        mockChildList.add(mockChild);

        Mockito.when(mockChildRepository.findByName("박어린이")).thenReturn(mockChildList);

        List<ChildDto> resultList = mockChildService.findByName("박어린이");

        resultList.forEach(result -> assertThat(result)
                .extracting(ChildDto::getId, ChildDto::getName)
                .containsExactly(1L, "박어린이"));
    }

    @Test
    @DisplayName("생월자 원아 가져오기 테스트")
    public void findChildWithBirthMonthMock() {
        Child firstChild = Child.builder()
                .id(1L)
                .birthday(LocalDate.of(2023,3,4))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .build();

        Child secondChild = Child.builder()
                .id(2L)
                .birthday(LocalDate.of(2024,3,17))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .build();

        Child thirdChild = Child.builder()
                .id(3L)
                .birthday(LocalDate.of(2025, 12, 7))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .build();

        List<Child> resultList = Arrays.asList(firstChild, secondChild);

        Mockito.when(mockChildRepository.findChildWithBirthMonth(3)).thenReturn(resultList);

        List<ChildDto> result = mockChildService.findChildWithBirthMonth(3);

        assertThat(result)
                .hasSize(2) // 리스트 크기 검증
                .extracting(ChildDto::getId) // ID 필드 추출
                .containsExactlyInAnyOrder(1L, 2L); // ID가 1L과 2L인지 검증

        assertThat(result)
                .extracting(ChildDto::getBirthday) // 생일 필드 추출
                .allMatch(birthday -> birthday.getMonthValue() == 3); // 모든 생일이 3월인지 검증

        assertThat(result)
                .extracting(ChildDto::getId)
                .doesNotContain(thirdChild.getId()); // thirdChild의 ID가 포함되지 않았는지 검증
    }

}
