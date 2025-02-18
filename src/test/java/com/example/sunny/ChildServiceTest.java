package com.example.sunny;

import com.example.sunny.code.SunnyCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.model.embedded.Address;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.repository.MeetingLoactionRepository;
import com.example.sunny.service.impl.ChildServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    public void findByNameTest() {
        Child mockChild = Child.builder()
                .id(1L)
                .name("박승훈")
                .build();

        Mockito.when(mockChildRepository.findByname("박승훈")).thenReturn(mockChild);

        ChildDto result = mockChildService.findByName("박승훈");

        assertNotNull(result);
        assertEquals("박승훈", result.getName());
    }

    @Test
    public void findChildWithBirthMonth() {
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

        assertNotNull(result);
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    public void checkChild() {
        Child findingChild = saveChild();
        List<Child> children = childRepository.checkChild(findingChild);

        assertNotNull(children);
        assertEquals("박승훈", children.get(0).getName());
        assertEquals("새싹반", children.get(0).getClassName());
    }

    private Child saveChild() {
        return childRepository.save(Child.builder()
                .id(1L)
                .address(Address.builder().address("사우동").detailAddress("산호아파트").zipCode("11111").build())
                .name("박승훈")
                .birthday(LocalDate.of(2023, 5, 24))
                .status("재원")
                .admissionDate(LocalDate.of(2024, 3, 2))
                .className("새싹반")
                .build());
    }

}
