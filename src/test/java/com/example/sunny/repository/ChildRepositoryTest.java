package com.example.sunny.repository;

import com.example.sunny.BootstrapData;
import com.example.sunny.code.SunnyCode;
import com.example.sunny.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ChildRepositoryTest {
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private SunnyRideRepository sunnyRideRepository;
    @Autowired
    private MeetingLoactionRepository meetingLoactionRepository;
    @Autowired
    private BootstrapData bootstrapData;
    
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
        MeetingLocation meetingLocationAm = meetingLoactionRepository.save(MeetingLocation.builder()
                .time("10:00")
                .name("김포시청")
                .sunnyRide(sunnyRideAm)
                .comment("~~~")
                .build());

        MeetingLocation meetingLocationPm = meetingLoactionRepository.save(MeetingLocation.builder()
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
    }

    @Test
    public void findAllWithParentsTest() {
        //원아 모든 정보 포함해서 등록
        bootstrapData.makeTestData(3);

        //전체 원아 조회와 부모와 함께 조회 시의 수 비교
        List<Child> all = childRepository.findAll();
        List<Child> allWithParents = childRepository.findAllWithParents();
        assertThat(all.size()).isEqualTo(allWithParents.size());
    }

    @Test
    public void findAllWithRidesTest() {
        //원아 모든 정보 포함해서 등록
        bootstrapData.makeTestData(3);

        //전체 원아 조회와 차량과 함께 조회 시의 수 비교
        List<Child> all = childRepository.findAll();
        List<Child> allWithRide = childRepository.findAllWithRide();
        assertThat(all.size()).isEqualTo(allWithRide.size());

    }
}
