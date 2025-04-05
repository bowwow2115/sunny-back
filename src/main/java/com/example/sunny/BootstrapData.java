package com.example.sunny;

import com.example.sunny.code.SunnyCode;
import com.example.sunny.model.User;
import com.example.sunny.model.dto.*;
import com.example.sunny.model.embedded.Address;
import com.example.sunny.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootstrapData implements CommandLineRunner {

    private final UserService userService;
    private final SunnyRideService sunnyRideService;
    private final SunnyClassServcie sunnyClassServcie;
    private final ChildService childService;
    private final MeetingLoactionService meetingLoactionService;

    private Random random = new Random();



    @Override
    public void run(String... args) throws Exception {

        User admin = userService.findUserByUserId("admin");
        if(admin == null) {
            UserDto user = UserDto.builder()
                    .password("admin")
                    .userId("admin")
                    .userName("관리자")
                    .role(SunnyCode.ROLE_GENERAL_ADMIN)
                    .status(true)
                    .email("")
                    .telephone("")
                    .build();

            UserDto userDto = userService.create(user);
            if(userDto != null) log.info("어드민 생성");
        }
//        makeTestData(10);
    }


    public List<ChildDto> makeTestData(int dataSize) {
        String[] classNames = {"꽃잎반", "열매반", "씨앗반"};


        String[] lastNames = {"김", "박", "이", "최", "정", "강", "조", "윤", "장", "임", "남"};
        String[] firstNames = {
                "어린이1", "어린이2", "어린이3", "어린이4", "어린이5",
                "어린이6", "어린이7", "어린이8", "어린이9", "어린이10",
                "어린이11", "어린이12", "어린이13", "어린이14", "어린이15",
                "어린이16", "어린이17", "어린이18", "어린이19", "어린이20",
                "어린이21", "어린이22", "어린이23", "어린이24", "어린이25",
                "어린이26", "어린이27", "어린이28", "어린이29", "어린이30"
        };

        String[] amRideTimes = {
                "07:30", "07:40", "07:50", "08:00", "08:10",
                "08:20", "08:30", "08:40", "08:50", "09:00",
                "09:10", "09:20", "09:30", "07:35", "07:45",
                "07:55", "08:05", "08:15", "08:25", "08:35",
                "08:45", "08:55", "09:05", "09:15", "09:25",
                "07:32", "07:42", "07:52", "08:02", "08:12"
        };
        String[] pmRideTimes = {
                "16:00", "16:10", "16:20", "16:30", "16:40",
                "16:50", "17:00", "17:10", "17:20", "17:30",
                "17:40", "17:50", "18:00", "16:05", "16:15",
                "16:25", "16:35", "16:45", "16:55", "17:05",
                "17:15", "17:25", "17:35", "17:45", "17:55",
                "16:02", "16:12", "16:22", "16:32", "16:42"
        };
        String[] addresses = {
                "서울시 종로구", "서울시 강남구", "서울시 서초구", "서울시 마포구", "서울시 은평구",
                "서울시 동대문구", "서울시 성북구", "서울시 동작구", "서울시 관악구", "서울시 송파구",
                "서울시 강동구", "서울시 강서구", "서울시 금천구", "서울시 양천구", "서울시 영등포구",
                "서울시 중랑구", "서울시 구로구", "서울시 도봉구", "서울시 성동구", "서울시 광진구",
                "서울시 서대문구", "서울시 종로구", "서울시 강남구", "서울시 서초구", "서울시 마포구",
                "서울시 은평구", "서울시 동대문구", "서울시 성북구", "서울시 동작구", "서울시 관악구"
        };
        String[] detailAddresses = {
                "무슨아파트 101동 102호", "무슨아파트 202동 203호", "0000번지 304동 305호", "아파트 1동 1호", "아파트 2동 2호",
                "아파트 3동 3호", "아파트 4동 4호", "아파트 5동 5호", "아파트 6동 6호", "아파트 7동 7호",
                "아파트 8동 8호", "아파트 9동 9호", "아파트 10동 10호", "아파트 11동 11호", "아파트 12동 12호",
                "아파트 13동 13호", "아파트 14동 14호", "아파트 15동 15호", "아파트 16동 16호", "아파트 17동 17호",
                "아파트 18동 18호", "아파트 19동 19호", "아파트 20동 20호", "아파트 21동 21호", "아파트 22동 22호",
                "아파트 23동 23호", "아파트 24동 24호", "아파트 25동 25호", "아파트 26동 26호", "아파트 27동 27호"
        };
        String[] zipCodes = {
                "21027", "12345", "67890", "10101", "20202",
                "30303", "40404", "50505", "60606", "70707",
                "80808", "90909", "10010", "11011", "12012",
                "13013", "14014", "15015", "16016", "17017",
                "18018", "19019", "20020", "21021", "22022",
                "23023", "24024", "25025", "26026", "27027"
        };

        String[] places = {
                "김포공항",
                "롯데몰 김포공항점",
                "방화근린공원",
                "화곡본동 주민센터",
                "마곡나루역",
                "등촌역",
                "가양역",
                "발산역",
                "우장산역",
                "화곡역",
                "강서구청",
                "화곡동 시장",
                "염창동 한강공원",
                "등촌동 주민센터",
                "가양1동 주민센터",
                "공항동 주민센터",
                "염창중학교",
                "방화동 롯데시네마",
                "가양도서관",
                "마곡중학교"
        };

        String[][] parentNames = {
                {"김아버지1", "박어머니1"}, {"김아버지2", "박어머니2"}, {"김아버지3", "박어머니3"}, {"김아버지4", "박어머니4"}, {"김아버지5", "박어머니5"},
                {"김아버지6", "박어머니6"}, {"김아버지7", "박어머니7"}, {"김아버지8", "박어머니8"}, {"김아버지9", "박어머니9"}, {"김아버지10", "박어머니10"},
                {"김아버지11", "박어머니11"}, {"김아버지12", "박어머니12"}, {"김아버지13", "박어머니13"}, {"김아버지14", "박어머니14"}, {"김아버지15", "박어머니15"},
                {"김아버지16", "박어머니16"}, {"김아버지17", "박어머니17"}, {"김아버지18", "박어머니18"}, {"김아버지19", "박어머니19"}, {"김아버지20", "박어머니20"},
                {"김아버지21", "박어머니21"}, {"김아버지22", "박어머니22"}, {"김아버지23", "박어머니23"}, {"김아버지24", "박어머니24"}, {"김아버지25", "박어머니25"},
                {"김아버지26", "박어머니26"}, {"김아버지27", "박어머니27"}, {"김아버지28", "박어머니28"}, {"김아버지29", "박어머니29"}, {"김아버지30", "박어머니30"}
        };
        String[][] parentTelephones = {
                {"010-1111-1111", "010-2222-2222"}, {"010-3333-3333", "010-4444-4444"}, {"010-5555-5555", "010-6666-6666"}, {"010-7777-7777", "010-8888-8888"},
                {"010-9999-9999", "010-0000-0000"}, {"010-1212-1212", "010-2323-2323"}, {"010-3434-3434", "010-4545-4545"}, {"010-5656-5656", "010-6767-6767"},
                {"010-7878-7878", "010-8989-8989"}, {"010-9090-9090", "010-1010-1010"}, {"010-1112-1112", "010-2223-2223"}, {"010-3334-3334", "010-4445-4445"},
                {"010-5556-5556", "010-6667-6667"}, {"010-7778-7778", "010-8889-8889"}, {"010-9990-9990", "010-0001-0001"}, {"010-1213-1213", "010-2324-2324"},
                {"010-3435-3435", "010-4546-4546"}, {"010-5657-5657", "010-6768-6768"}, {"010-7879-7879", "010-8990-8990"}, {"010-9091-9091", "010-1011-1011"},
                {"010-1113-1113", "010-2224-2224"}, {"010-3335-3335", "010-4446-4446"}, {"010-5557-5557", "010-6668-6668"}, {"010-7779-7779", "010-8880-8880"},
                {"010-9991-9991", "010-0002-0002"}, {"010-1214-1214", "010-2325-2325"}, {"010-3436-3436", "010-4547-4547"}, {"010-5658-5658", "010-6769-6769"},
                {"010-7880-7880", "010-8991-8991"}, {"010-9092-9092", "010-1012-1012"}
        };
        String[] parentRelations = {"부", "모"};




        //오전차량 생성
        List<SunnyRideDto> amRideList = new ArrayList<>();

        SunnyRideDto sunnyRideAm = SunnyRideDto.builder()
                .comment("~~~")
                .name("김포 방향 버스")
                .time("09:00")
                .isAm(true)
                .build();

        SunnyRideDto amRide = sunnyRideService.create(sunnyRideAm);
        if(amRide != null) log.info("오전차량 생성");
        amRideList.add(amRide);

        sunnyRideAm = SunnyRideDto.builder()
                .comment("~~~")
                .name("서울 방향 봉고")
                .time("09:10")
                .isAm(true)
                .build();

        amRide = sunnyRideService.create(sunnyRideAm);
        if(amRide != null) log.info("오전차량 생성");
        amRideList.add(amRide);

        //오후차량 생성
        List<SunnyRideDto> pmRideList = new ArrayList<>();

        SunnyRideDto sunnyRidePm = SunnyRideDto.builder()
                .comment("~~~")
                .name("서울 방향 버스")
                .time("16:00")
                .isAm(false)
                .build();

        SunnyRideDto pmRide = sunnyRideService.create(sunnyRidePm);
        if(pmRide != null) log.info("오후차량 생성");
        pmRideList.add(pmRide);

        sunnyRidePm = SunnyRideDto.builder()
                .comment("~~~")
                .name("김포 방향 봉고")
                .time("16:30")
                .isAm(false)
                .build();

        pmRide = sunnyRideService.create(sunnyRidePm);
        if(pmRide != null) log.info("오후차량 생성");
        pmRideList.add(pmRide);

        List<MeetingLocationDto> amMeetingLocationList = new ArrayList<>();
        List<MeetingLocationDto> pmMeetingLocationList = new ArrayList<>();
        //집결장소 생성
        for(int i=0; i< places.length; i++) {
            if(i%2 == 1) {
                MeetingLocationDto meetingLocationDto = MeetingLocationDto.builder()
                        .time(amRideTimes[i])
                        .name(places[i])
                        .sunnyRide(amRideList.get(random.nextInt(amRideList.size())))
                        .comment("~~~")
                        .build();
                MeetingLocationDto result = meetingLoactionService.create(meetingLocationDto);
                if(result != null) log.info("오전 집결장소 생성");
                amMeetingLocationList.add(result);
            } else {
                MeetingLocationDto meetingLocationDto = MeetingLocationDto.builder()
                        .time(pmRideTimes[i])
                        .name(places[i])
                        .sunnyRide(pmRideList.get(random.nextInt(pmRideList.size())))
                        .comment("~~~~")
                        .build();
                MeetingLocationDto result = meetingLoactionService.create(meetingLocationDto);
                if(result != null) log.info("오후 집결장소 생성");

                pmMeetingLocationList.add(result);
            }
        }

        //반생성
        SunnyClassDto sunnyClassDto = SunnyClassDto.builder()
                .name("열매반")
                .build();

        SunnyClassDto sunnyClass = sunnyClassServcie.create(sunnyClassDto);
        if(sunnyClass != null) log.info("반 생성");

        SunnyClassDto sunnyClassDto2 = SunnyClassDto.builder()
                .name("씨앗반")
                .build();

        SunnyClassDto sunnyClass2 = sunnyClassServcie.create(sunnyClassDto2);
        if(sunnyClass2 != null) log.info("반 생성");

        SunnyClassDto sunnyClassDto3 = SunnyClassDto.builder()
                .name("꽃잎반")
                .build();

        SunnyClassDto sunnyClass3 = sunnyClassServcie.create(sunnyClassDto3);
        if(sunnyClass3 != null) log.info("반 생성");

        List<ChildDto> childDtoList = new ArrayList<>();

        for (int i = 0; i < dataSize; i++) {
            // 성과 이름을 결합하여 이름 생성
            String childName = lastNames[i % lastNames.length] + firstNames[i];

            // Address 초기화
            Address address = new Address();
            address.setAddress(addresses[i]);
            address.setDetailAddress(detailAddresses[i]);
            address.setZipCode(zipCodes[i]);

            // ParentsDto 초기화
            List<ParentsDto> parentsDtoList = new ArrayList<>();
            for (int j = 0; j < parentRelations.length; j++) {
                ParentsDto parent = ParentsDto.builder()
                        .telephone(parentTelephones[i][j])
                        .name(parentNames[i][j])
                        .relation(parentRelations[j])
                        .build();
                parentsDtoList.add(parent);
            }

            LocalDate admissionDate = generateRandomDate(2021, 2024);
            LocalDate birthday = generateRandomDate(2017, 2020);

            // ChildDto 초기화
            ChildDto childDto = ChildDto.builder()
                    .address(address)
                    .admissionDate(admissionDate)
                    .birthday(birthday)
                    .className(classNames[i % classNames.length])
                    .status("재원")
                    .name(childName)
                    .parentList(parentsDtoList)
                    .childRideList(new ArrayList<>())
                    .build();

            // ChildRideDto 초기화
            ChilMeetingLocationDto sunnyChildRideAm = ChilMeetingLocationDto.builder()
//                    .time(amRideTimes[i])
                    .meetingLocation(amMeetingLocationList.get(i % amMeetingLocationList.size()))
                    .build();

            ChilMeetingLocationDto sunnyChildRidePm = ChilMeetingLocationDto.builder()
                    .meetingLocation(pmMeetingLocationList.get(i % pmMeetingLocationList.size()))
                    .build();

            // ChildDto에 승차 정보 설정
            childDto.getChildRideList().add(sunnyChildRideAm);
            childDto.getChildRideList().add(sunnyChildRidePm);

            // ChildDto 생성 및 리스트에 추가
            ChildDto child = childService.create(childDto);
            if (child != null) log.info("원아생성: " + child.getName());

            childDtoList.add(child);
        }
        return childDtoList;
    }

    private LocalDate generateRandomDate(int startYear, int endYear) {
        int year = random.nextInt(endYear - startYear + 1) + startYear;
        int dayOfYear = random.nextInt(365) + 1;
        return LocalDate.ofYearDay(year, dayOfYear);
    }


}
