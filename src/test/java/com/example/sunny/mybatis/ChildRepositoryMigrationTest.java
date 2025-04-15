package com.example.sunny.mybatis;

import com.example.sunny.code.SunnyCode;
import com.example.sunny.model.*;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.repository.MeetingLocationRepository;
import com.example.sunny.repository.SunnyRideRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JPA -> MyBatis 마이그레이션 작업테스트 용
 * 클래스명 *Repository인 경우 JPA DAO, *Mapper인 경우 MyBatis DAO
 *
 * JPA 환경에서는 insert, update 시에 반환하는 데이터를 DB의 데이터와 동일하다고 사용
 * MyBatis 환경에서는 새로 select 해와야 함
 * (MyBatis로 마이그레이션 하기 떄문에 작업 완료 후에 JPA관련 코드 삭제 쉽게하기 위해 데이터 세팅 작업은 MyBatis로 함)
 */
@SpringBootTest
@Transactional
public class ChildRepositoryMigrationTest {
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private SunnyRideRepository sunnyRideRepository;
    @Autowired
    private MeetingLocationRepository meetingLocationRepository;
    
    @Test
    @DisplayName("원아 중복 테스트")
    public void checkChild() {
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
                .name("이어린이")
                .birthday(LocalDate.of(2020, 5, 4))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .admissionDate(LocalDate.of(2023, 11, 1))
                .className("열매반")
                .build());

        //상태(status) 기준으로 가져옴
        List<Child> attendingChildren = childRepository.findAttendingChildren();

        assertThat(attendingChildren)
                .extracting(Child::getStatus)
                .allMatch(status -> status.equals(SunnyCode.CHILD_STATUS_ATTENDING));
    }

    @Test
    @DisplayName("반업데이트")
    public void updateChildrenClassTest() {
        Child oldChild = childRepository.save(Child.builder()
                .id(1L)
                .name("박어린이")
                .birthday(LocalDate.of(2023, 5, 24))
                .status(SunnyCode.CHILD_STATUS_ATTENDING)
                .admissionDate(LocalDate.of(2024, 3, 2))
                .className("새싹반")
                .build());

//        oldChild.setClassName("꽃잎반");

        Child newChild = childRepository.save(oldChild);

        assertThat(newChild.getClassName()).isEqualTo(oldChild.getClassName());
    }

    @Test
    @DisplayName("원아 등록(차량, 승하차장소, 부모등록 포함")
    public void createTest() {
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
        
        //원아와 승하차장소 매핑테이블에 원아를 통해 인서트
        List<ChildMeetingLocation> childMeetingLocationList = new ArrayList<>();

        ChildMeetingLocation childMeetingLocationAm = ChildMeetingLocation.builder()
                .meetingLocation(meetingLocationAm)
                .build();

        ChildMeetingLocation childMeetingLocationPm = ChildMeetingLocation.builder()
                .meetingLocation(meetingLocationPm)
                .build();

        childMeetingLocationList.add(childMeetingLocationAm);
        childMeetingLocationList.add(childMeetingLocationPm);
        
        //부모 원아를 통해 인서트
        List<Parents> parentsList = new ArrayList<>();

        Parents parents = Parents.builder()
                .telephone("010-1234-1234")
                .name("박어린이 아빠")
                .relation("부")
                .build();

        parentsList.add(parents);

        Child input = Child.builder()
                .id(1L)
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

        //부모 확인
        assertThat(result.getParents().get(0)).isNotNull();
        //차량 확인
        assertThat(result.getChildMeetingLocations().get(0)).isNotNull();

//        assertThat(result.get)

    }

    private List<Child> createRandomChildren(int count) {
        return null;
    }

}
