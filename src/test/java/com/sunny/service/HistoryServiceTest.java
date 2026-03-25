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
//@Transactional
public class HistoryServiceTest {
    @Autowired
    private ChildService childService;
    @Autowired
    private ParentsService parentsService;
    @Autowired
    private SunnyClassServcie sunnyClassServcie;
    @Autowired
    private SunnyRideService sunnyRideService;
    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    /**
     * BaseEntity의 createDate PrePersist 어노테이션을 해제해야 정상작동
     */
//    @Test
//    public void deleteByCreatedDateBatchTest() {
//        // given
//        LocalDate now = LocalDate.now();
//        for (int i = 0; i < 10; i++) {
//            historyService.create(BusinessHistoryDto.builder()
//                    .targetType("Test")
//                    .createdDate(now.minusDays(i).atStartOfDay())
//                    .build());
//        }
//
//        // when
//        long deletedCount = historyService.deleteByCreatedDateBatch(now.minusDays(7).atStartOfDay(), 10);
//
//        // then
//        assertThat(deletedCount).isEqualTo(2);
//    }

    @Test
    public void updateChildrenClassOfHistoryTest() {
        List<ChildDto> childList = updateChildrenClass();

        childList.forEach(childDto -> {
            List<BusinessHistoryDto> historyDtos = historyService.findByTargetIdAndType(childDto.getId(), "Child");
            //  create, update, find 총 3번의 이력이 남아야 한다.
            //  api를 통한 접근이 아니라 newValue 등의 데이터가 정상적으로 쌓이지 않음
            assertThat(historyDtos).size().isEqualTo(3);
        });
    }

    @Test
    public void getHistoryByCondition_byName() {
        insertChildren();
        Pageable pageable = Pageable.ofSize(10);
        HistorySearchCondition historySearchCondition = HistorySearchCondition.builder()
                .targetType("Child")
                .name("김어린이")
                .orderBy("asc")
                .build();
        Page<BusinessHistoryDto> historyByCondition = historyService.getHistoryByCondition(pageable, historySearchCondition);
        assertThat(historyByCondition).size().isGreaterThanOrEqualTo(2);
        historyByCondition.forEach(history -> {
            assertThat(history.getTargetType()).isEqualTo("Child");
            assertThat(history.getName()).contains("김어린이");
        });
    }

    private List<ChildDto> insertChildren() {
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
        assertThat(childList).extracting(ChildDto::getId)
                .allSatisfy(id -> assertThat(id).isNotNull());

        return childList;
    }

    private List<ChildDto> updateChildrenClass() {
        List<ChildDto> childList = insertChildren();
        childService.updateChildrenClass(childList, "반2");

        childList.forEach(child -> {
            ChildDto byId = childService.findById(child.getId());
            assertThat(byId.getClassName()).isEqualTo("반2");
        });
        return childList;
    }
}
