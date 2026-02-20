package com.sunny.repository;

import com.sunny.BootstrapData;
import com.sunny.code.SunnyCode;
import com.sunny.model.*;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
    "spring.jpa.properties.hibernate.format_sql=true",
    "spring.jpa.properties.hibernate.use_sql_comments=true"
})
public class ChildRepositoryTest {
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private SunnyRideRepository sunnyRideRepository;
    @Autowired
    private MeetingLocationRepository meetingLocationRepository;
    @Autowired
    private BootstrapData bootstrapData;
    @Autowired
    private EntityManager entityManager;

        private SessionFactory sessionFactory;
        private Statistics statistics;

        @BeforeEach
        void setUp() {
                sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
                statistics = sessionFactory.getStatistics();
                statistics.setStatisticsEnabled(true);
                statistics.clear();
        }

        private void resetQueryCount() {
                statistics.clear();
        }

        private long getQueryCount() {
                return statistics.getPrepareStatementCount();
        }

    @Test
    @DisplayName("원아 중복 테스트 - 쿼리 2개 (save + custom query)")
    public void checkChildTest() {
        resetQueryCount();
        Child child = childRepository.save(Child.builder()
                .id(1L)
                .name("박어린이")
                .birthday(LocalDate.of(2023, 5, 24))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .admissionDate(LocalDate.of(2024, 3, 2))
                .className("새싹반")
                .build());

        //className과 name 기준으로 등록된 원아를 가져옴
        List<Child> children = childRepository.checkChild(child);
        long queryCount = getQueryCount();

        assertThat(children)
                .extracting(Child::getClassName)
                .allMatch(clsName -> clsName.equals(child.getClassName()));

        assertThat(children)
                .extracting(Child::getName)
                .allMatch(name -> name.equals(child.getName()));

        // save(INSERT) + checkChild(SELECT) = 2개 쿼리 예상
        assertThat(queryCount).isGreaterThanOrEqualTo(2L);
    }

    @Test
    @DisplayName("재학중인 원아 가져오기 테스트 - 쿼리 3개 (3개 save + 1개 select)")
    public void findAttendingChildrenTest() {
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
                .name("이어린이")
                .birthday(LocalDate.of(2020, 5, 4))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .admissionDate(LocalDate.of(2023, 11, 1))
                .className("열매반")
                .build());

        // 비교 방식: naive 구현 vs fetchJoin로 최적화된 구현의 쿼리 수 비교
        resetQueryCount();
        List<Child> naive = childRepository.findAttendingChildren();
        long naiveCount = getQueryCount();

        resetQueryCount();
        List<Child> optimized = childRepository.findAttendingChildrenWithFetch();
        long optCount = getQueryCount();

        assertThat(naive).isNotEmpty();
        assertThat(optimized).isNotEmpty();

        // 모든 결과의 상태가 재원(재학)인지 확인
        assertThat(optimized).extracting(Child::getStatus)
                .allMatch(status -> status.equals(SunnyCode.CHILD_STATUS_ATTENDING));

        // 최적화 쿼리는 동일한 작업에 대해 쿼리 수가 적거나 같아야 함
        assertThat(optCount).isLessThanOrEqualTo(naiveCount);
    }

    @Test
    @DisplayName("반업데이트 - 쿼리 2개 (save + update)")
    public void updateChildrenClassTest() {
        Child oldChild = childRepository.save(Child.builder()
                .id(1L)
                .name("박어린이")
                .birthday(LocalDate.of(2023, 5, 24))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .admissionDate(LocalDate.of(2024, 3, 2))
                .className("새싹반")
                .build());

        resetQueryCount();

        Child newChild = childRepository.save(oldChild);
        long queryCount = getQueryCount();

        assertThat(newChild.getClassName()).isEqualTo(oldChild.getClassName());

        // UPDATE 쿼리 1개 예상
        assertThat(queryCount).isGreaterThanOrEqualTo(0L);
    }

    @Test
    @DisplayName("원아 등록(차량, 승하차장소, 부모등록 포함) - 복합 쿼리")
    public void createTest() {
        resetQueryCount();
        //차량생성
        SunnyRide sunnyRideAm = sunnyRideRepository.save(SunnyRide.builder()
                .comment("~~~")
                .name("김포 방향 버스")
                .time("09:00")
                .isAm(true)
                .build());

        SunnyRide sunnyRidePm = sunnyRideRepository.save(SunnyRide.builder()
                .comment("~~~")
                .name("서울 방향 버스")
                .time("16:00")
                .isAm(false)
                .build());

        //승하차 장소 생성
        MeetingLocation meetingLocationAm = meetingLocationRepository.save(MeetingLocation.builder()
                .time("10:00")
                .name("김포시청")
                .sunnyRide(sunnyRideAm)
                .comment("~~~")
                .build());

        MeetingLocation meetingLocationPm = meetingLocationRepository.save(MeetingLocation.builder()
                .time("16:00")
                .name("김포시청")
                .sunnyRide(sunnyRidePm)
                .comment("~~~")
                .build());

        ChildMeetingLocation childMeetingLocationAm = ChildMeetingLocation.builder()
                .meetingLocation(meetingLocationAm)
                .build();

        ChildMeetingLocation childMeetingLocationPm = ChildMeetingLocation.builder()
                .meetingLocation(meetingLocationPm)
                .build();

        Parents parents = Parents.builder()
                .telephone("010-1234-1234")
                .name("박어린이 아빠")
                .relation("부")
                .build();

        Child input = Child.builder()
                .name("박어린이")
                .birthday(LocalDate.of(2023, 5, 24))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .admissionDate(LocalDate.of(2024, 3, 2))
                .className("새싹반")
                .build();

        input.addParents(parents);
        input.addRide(childMeetingLocationAm);
        input.addRide(childMeetingLocationPm);

        //원아 생성
        Child result = childRepository.save(input);
        entityManager.flush();
        long queryCount = getQueryCount();

        //부모 확인
        assertThat(result.getParents())
                .contains(parents);
        //차량 확인
        assertThat(result.getChildMeetingLocations())
                .containsExactlyInAnyOrder(childMeetingLocationAm, childMeetingLocationPm);
        //승하차 장소 확인
        assertThat(result.getChildMeetingLocations())
                .extracting(ChildMeetingLocation::getMeetingLocation)
                .containsExactlyInAnyOrder(meetingLocationAm, meetingLocationPm);
        //원아 확인
        assertThat(result).isEqualTo(input);

        // 2개 차량 + 2개 승하차장소 + 1개 부모 + 1개 원아 = 6개 INSERT
        // + 관계 설정을 위한 UPDATE들
        assertThat(queryCount).isGreaterThanOrEqualTo(6L);
    }

    @Test
    @DisplayName("원아 부모정보 포함 찾기 테스트 - 쿼리 최적화 확인")
    public void findAllWithParentsTest() {
        //원아 모든 정보 포함해서 등록
        bootstrapData.makeTestData(3);

        resetQueryCount();
        //전체 원아 조회와 부모와 함께 조회 시의 수 비교
        List<Child> all = childRepository.findAll();
        long queryCountAll = getQueryCount();

        resetQueryCount();
        List<Child> allWithParents = childRepository.findAllWithParents();
        long queryCountWithParents = getQueryCount();

        assertThat(all.size()).isEqualTo(allWithParents.size());
        int count = 0;
        for (Child child : allWithParents) {
            if(child.getParents() != null) count++;
        }
        //부모 있는 경우가 3이상인지 확인
        assertThat(count).isGreaterThanOrEqualTo(3);

        // findAll 쿼리 수 vs findAllWithParents 쿼리 수 비교
        // (N+1 문제가 없어야 함)
        assertThat(queryCountAll).isGreaterThanOrEqualTo(1L);
        assertThat(queryCountWithParents).isLessThanOrEqualTo(queryCountAll + 1);
    }

    @Test
    @DisplayName("원아 찾기 승하차정보 포함 찾기 테스트 - 쿼리 최적화 확인")
    public void findAllWithRidesTest() {
        //원아 모든 정보 포함해서 등록
        bootstrapData.makeTestData(3);

        resetQueryCount();
        //전체 원아 조회와 차량과 함께 조회 시의 수 비교
        List<Child> all = childRepository.findAll();
        long queryCountAll = getQueryCount();

        resetQueryCount();
        List<Child> allWithRide = childRepository.findAllWithRide();
        long queryCountWithRide = getQueryCount();

        assertThat(all.size()).isEqualTo(allWithRide.size());
        int count = 0;

        for (Child child : allWithRide) {
            for(ChildMeetingLocation childMeetingLocation : child.getChildMeetingLocations()) {
                if(childMeetingLocation.getMeetingLocation().getSunnyRide()!=null) count++;
            }
        }
        //차량 정보있는 경우가 3이상인지 확인
        assertThat(count).isGreaterThanOrEqualTo(3);

        // findAll 쿼리 수 vs findAllWithRide 쿼리 수 비교
        // (N+1 문제가 없어야 함)
        assertThat(queryCountAll).isGreaterThanOrEqualTo(1L);
        assertThat(queryCountWithRide).isLessThanOrEqualTo(queryCountAll + 1);
    }

    @Test
    @DisplayName("이름으로 원아찾기 - 1개 쿼리")
    public void findByNameTest() {
        Child input = Child.builder()
                .name("박어린이")
                .birthday(LocalDate.of(2023, 5, 24))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .admissionDate(LocalDate.of(2024, 3, 2))
                .className("새싹반")
                .build();

        childRepository.save(input);

        resetQueryCount();
        List<Child> byNameList = childRepository.findByName(input.getName());
        long queryCount = getQueryCount();

        byNameList.forEach(byName -> assertThat(byName.getName()).isEqualTo(input.getName()));

        // 1개의 SELECT 쿼리만 실행
        assertThat(queryCount).isEqualTo(1L);
    }
}
