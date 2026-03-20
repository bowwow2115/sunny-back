package com.sunny.service;

import com.sunny.model.BusinessHistory;
import com.sunny.model.dto.ChildDto;
import org.assertj.core.api.Assertions;
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
public class HistoryServiceTest {
    @Autowired
    private ChildService childService;

    @Autowired
    private HistoryService historyService;

    /**
     * BaseEntity의 createDate PrePersist 어노테이션을 해제해야 정상작동
     */
    @Test
    public void deleteByCreatedDateBatchTest() {
        // given
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 10; i++) {
            historyService.create(BusinessHistory.builder()
                    .targetType("Test")
                    .createdBy("testUser")
                    .createdDate(now.minusDays(i).atStartOfDay())
                    .modifiedDate(now.atStartOfDay())
                    .build());
        }

        // when
        long deletedCount = historyService.deleteByCreatedDateBatch(now.minusDays(7).atStartOfDay(), 10);

        // then
        assertThat(deletedCount).isEqualTo(2);

    }

    @Test
    public void updateChildrenClassOfHistoryTest() {
        List<ChildDto> childList = updateChildrenClassTest();
        ChildDto childDto1 = childList.get(0);
        ChildDto childDto2 = childList.get(1);

        List<BusinessHistory> child1History = historyService.findByTargetIdAndType(childDto1.getId(), "Child");
        List<BusinessHistory> child2History = historyService.findByTargetIdAndType(childDto2.getId(), "Child");

        //  create, update, find 총 3번의 이력이 남아야 한다.
        //  api를 통한 접근이 아니라 newValue, mehthod등의 데이터가 정상적으로 쌓이지 않음
        Assertions.assertThat(child1History).size().isEqualTo(3);
        Assertions.assertThat(child2History).size().isEqualTo(3);

    }

    private List<ChildDto> updateChildrenClassTest() {
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

        return childList;
    }
}
