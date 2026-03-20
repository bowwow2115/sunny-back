package com.sunny.service;

import com.sunny.model.dto.ChildDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ChildServiceTest {
    @Autowired
    private ChildService childService;

    @Test
    public void updateChildrenClassTest() {
        ChildDto child1 = childService.create(ChildDto.builder()
                .name("김어린이")
                .admissionDate(LocalDate.now())
                .birthday(LocalDate.now())
                .className("반")
                .build());

        ChildDto child2 = childService.create(ChildDto.builder()
                .name("김어린이2")
                .admissionDate(LocalDate.now())
                .birthday(LocalDate.now())
                .className("반")
                .build());

        List<ChildDto> childList =  new ArrayList<>();
        childList.add(child1);
        childList.add(child2);
        childService.updateChildrenClass(childList, "반2");

        ChildDto byId1 = childService.findById(child1.getId());
        ChildDto byId2 = childService.findById(child2.getId());

        assertThat(byId1.getClassName()).isEqualTo("반2");
        assertThat(byId2.getClassName()).isEqualTo("반2");
    }
}
