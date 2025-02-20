package com.example.sunny;

import com.example.sunny.code.SunnyCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.repository.MeetingLoactionRepository;
import com.example.sunny.service.impl.ChildServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JPA 환경에서는 inesrt, update시에 반환하는 데이터를 DB의 데이터와 동일하다고 사용
 * Mybatis 환경에서는 새로 select 해와야 함
 */
@SpringBootTest
@Transactional
public class ChildServiceTest {
    @Mock
    private ChildRepository mockChildRepository;
    @Mock
    private MeetingLoactionRepository mockMeetingLoactionRepository;

    @InjectMocks
    private ChildServiceImpl mockChildService;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private MeetingLoactionRepository meetingLoactionRepository;

    @Test
    @DisplayName("원아 이름으로 가져오기 테스트")
    public void findByNameMock() {
        Child mockChild = Child.builder()
                .id(1L)
                .name("박승훈")
                .build();

        Mockito.when(mockChildRepository.findByname("박승훈")).thenReturn(mockChild);

        ChildDto successResult = mockChildService.findByName("박승훈");

        assertThat(successResult.getId()).isEqualTo(1L);
    }

    /**
     * 참석중인 메소ㄷ
     */
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

        List<Child> result = mockChildRepository.findChildWithBirthMonth(3);

        assertThat(result)
//                .hasSize(2) // 리스트 크기 검증
                .extracting(Child::getId) // ID 필드 추출
                .containsExactlyInAnyOrder(1L, 2L); // ID가 1L과 2L인지 검증

        assertThat(result)
                .extracting(Child::getBirthday) // 생일 필드 추출
                .allMatch(birthday -> birthday.getMonthValue() == 3); // 모든 생일이 3월인지 검증

        assertThat(result)
                .extracting(Child::getId)
                .doesNotContain(thirdChild.getId()); // thirdChild의 ID가 포함되지 않았는지 검
    }

    @Test
    @DisplayName("원아 중복 테스트")
    public void checkChild() {
        Child child = childRepository.save(Child.builder()
                .id(1L)
                .name("박승훈")
                .birthday(LocalDate.of(2023, 5, 24))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .admissionDate(LocalDate.of(2024, 3, 2))
                .className("새싹반")
                .build());

        List<Child> children = childRepository.checkChild(child);

        assertThat(children)
                .extracting(Child::getClassName)
                .allMatch(clsName -> clsName.equals(child.getClassName()));

        assertThat(children)
                .extracting(Child::getName)
                .allMatch(name -> name.equals(child.getName()));
    }

    @Test
    @DisplayName("재학중인 원아 가져오기 테스트")
    public void getAttendingChildren() {
        childRepository.save(Child.builder()
                .id(1L)
                .name("박어린이")
                .birthday(LocalDate.of(2023, 5, 24))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .admissionDate(LocalDate.of(2024, 3, 2))
                .className("새싹반")
                .build());

        childRepository.save(Child.builder()
                .id(2L)
                .name("김어린이")
                .birthday(LocalDate.of(2022, 1, 12))
                .status("졸업")
                .admissionDate(LocalDate.of(2024, 3, 2))
                .className("꽃잎반")
                .build());

        childRepository.save(Child.builder()
                .id(3L)
                .name("김어린이")
                .birthday(LocalDate.of(2020, 5, 4))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .admissionDate(LocalDate.of(2023, 11, 1))
                .className("열매반")
                .build());

        List<Child> attendingChildren = childRepository.getAttendingChildren();

        assertThat(attendingChildren)
                .extracting(Child::getStatus)
                .allMatch(status -> status.equals(SunnyCode.CHILD_STATUS_ATTENDING));

    }

    @Test
    @DisplayName("반업데이트")
    public void updateChildrenClassTest() {
        Child origin = childRepository.save(Child.builder()
                .id(1L)
                .name("박어린이")
                .birthday(LocalDate.of(2023, 5, 24))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .admissionDate(LocalDate.of(2024, 3, 2))
                .className("새싹반")
                .build());

        origin.setClassName("꽃잎반");

        Child changedChild = childRepository.save(origin);

        assertThat(changedChild.getClassName()).isEqualTo(origin.getClassName());
    }



}
