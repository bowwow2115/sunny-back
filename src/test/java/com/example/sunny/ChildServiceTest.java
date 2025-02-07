package com.example.sunny;

import com.example.sunny.code.SunnyCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.repository.MeetingLoactionRepository;
import com.example.sunny.service.impl.ChildServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ChildServiceTest {
    @Mock
    private ChildRepository childRepository;
    @Mock
    private MeetingLoactionRepository meetingLoactionRepository;

    @InjectMocks
    private ChildServiceImpl childService;

    @Test
    public void findByNameTest() {
        Child mockChild = Child.builder()
                .id(1L)
                .name("박승훈")
                .build();

        Mockito.when(childRepository.findByname("박승훈")).thenReturn(mockChild);

        ChildDto result = childService.findByName("박승훈");

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

        Mockito.when(childRepository.findChildWithBirthMonth(3)).thenReturn(resultList);

//        child
    }
}
