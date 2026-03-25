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
