package com.sunny.service;

import com.sunny.model.dto.BusinessHistoryDto;
import com.sunny.model.dto.ChildDto;
import com.sunny.model.dto.HistorySearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HistoryServiceTest {
    @Autowired
    private ChildService childService;

    @Autowired
    private HistoryService historyService;

    @Test
    public void updateChildrenClassOfHistoryTest() {
        List<ChildDto> childList = updateChildrenClass();

        childList.forEach(childDto -> {
            List<BusinessHistoryDto> historyDtos = waitForHistory(childDto.getId(), "Child", 3);
            assertThat(historyDtos).hasSize(3);
        });
    }

    @Test
    public void getHistoryByCondition_byName() {
        insertChildren();

        Pageable pageable = Pageable.ofSize(10);
        HistorySearchCondition historySearchCondition = HistorySearchCondition.builder()
                .targetType("Child")
                .name("test-child")
                .orderBy("asc")
                .build();

        Page<BusinessHistoryDto> historyByCondition = historyService.getHistoryByCondition(pageable, historySearchCondition);
        assertThat(historyByCondition).size().isGreaterThanOrEqualTo(2);
        historyByCondition.forEach(history -> {
            assertThat(history.getTargetType()).isEqualTo("Child");
            assertThat(history.getName()).contains("test-child");
        });
    }

    private List<ChildDto> insertChildren() {
        ChildDto child1 = childService.create(ChildDto.builder()
                .name("test-child-1")
                .admissionDate(LocalDate.now())
                .birthday(LocalDate.now())
                .className("class-a")
                .build());

        ChildDto child2 = childService.create(ChildDto.builder()
                .name("test-child-2")
                .admissionDate(LocalDate.now())
                .birthday(LocalDate.now())
                .className("class-a")
                .build());

        List<ChildDto> childList = new ArrayList<>();
        childList.add(child1);
        childList.add(child2);

        assertThat(childList).extracting(ChildDto::getId)
                .allSatisfy(id -> assertThat(id).isNotNull());

        return childList;
    }

    private List<ChildDto> updateChildrenClass() {
        List<ChildDto> childList = insertChildren();
        childService.updateChildrenClass(childList, "class-b");

        childList.forEach(child -> {
            ChildDto byId = childService.findById(child.getId());
            assertThat(byId.getClassName()).isEqualTo("class-b");
        });
        return childList;
    }

    private List<BusinessHistoryDto> waitForHistory(Long targetId, String targetType, int expectedSize) {
        long deadline = System.currentTimeMillis() + 3000;
        List<BusinessHistoryDto> historyDtos = new ArrayList<>();

        while (System.currentTimeMillis() < deadline) {
            historyDtos = historyService.findByTargetIdAndType(targetId, targetType);
            if (historyDtos.size() >= expectedSize) {
                return historyDtos;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return historyDtos;
    }
}