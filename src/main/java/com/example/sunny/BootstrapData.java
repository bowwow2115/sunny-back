package com.example.sunny;

import com.example.sunny.code.SunnyCode;
import com.example.sunny.model.dto.*;
import com.example.sunny.model.embedded.Address;
import com.example.sunny.service.ChildService;
import com.example.sunny.service.SunnyClassServcie;
import com.example.sunny.service.SunnyRideService;
import com.example.sunny.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootstrapData implements CommandLineRunner {

    private final UserService userService;
    private final SunnyRideService sunnyRideService;
    private final SunnyClassServcie sunnyClassServcie;
    private final ChildService childService;

    @Override
    public void run(String... args) throws Exception {
        UserDto user = UserDto.builder()
                .password("test")
                .userId("test")
                .userName("관리자")
                .role(SunnyCode.ROLE_GENERAL_ADMIN)
                .status(true)
                .email("test@test.com")
                .telephone("010-1234-1234")
                .build();

        UserDto userDto = userService.create(user);
        if(userDto != null) log.info("유저생성");

        SunnyRideDto sunnyRideAm = SunnyRideDto.builder()
                .comment("코멘트")
                .name("아침 9시 코스")
                .time("09:00")
                .isAm(true)
                .build();

        SunnyRideDto amRide = sunnyRideService.create(sunnyRideAm);
        if(amRide != null) log.info("오전차량 생성");

        SunnyRideDto sunnyRidePm = SunnyRideDto.builder()
                .comment("코멘트")
                .name("낮 4시 코스")
                .time("16:00")
                .isAm(false)
                .build();

        SunnyRideDto pmRide = sunnyRideService.create(sunnyRidePm);
        if(pmRide != null) log.info("오후차량 생성");

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

        Address address = new Address();
        address.setAddress("서울시 종로구");
        address.setDetailAddress("547");
        address.setZipCode("21027");

        List<ParentsDto> parentsDtoList = new ArrayList<>();
        ParentsDto father = ParentsDto.builder()
                .telephone("010-2222-2222")
                .name("김뭐시기")
                .relation("부")
                .build();

        ParentsDto mother = ParentsDto.builder()
                .telephone("010-3333-3333")
                .name("박뭐시기")
                .relation("모")
                .build();

        parentsDtoList.add(father);
        parentsDtoList.add(mother);

        ChildDto childDto = ChildDto.builder()
                .address(address)
                .childCode("A00001")
                .admissionDate(LocalDate.of(2022, 05, 01))
                .birthday(LocalDate.of(2017, 02, 21))
                .className(sunnyClass.getName())
                .status(true)
                .name("김아무개")
                .parentList(parentsDtoList)
                .build();

        SunnyChildRideDto sunnyChildRideAm = SunnyChildRideDto.builder()
                .time("09:00")
                .comment("계단 옆")
                .sunnyRide(amRide)
                .build();

        SunnyChildRideDto sunnyChildRidePm = SunnyChildRideDto.builder()
                .time("16:00")
                .comment("아파트 뒷문")
                .sunnyRide(pmRide)
                .build();

        childDto.setAmRide(sunnyChildRideAm);
        childDto.setPmRide(sunnyChildRidePm);

        ChildDto child = childService.create(childDto);
        if(child != null) log.info("원아생성");

        Address address2 = new Address();
        address2.setAddress("서울시 종로구 본동");
        address2.setDetailAddress("한미아파트 103동 403호");
        address2.setZipCode("10101");

        List<ParentsDto> parentsDtoList2 = new ArrayList<>();
        ParentsDto father2 = ParentsDto.builder()
                .telephone("010-2222-2222")
                .name("고아빠")
                .relation("부")
                .build();

        ParentsDto mother2 = ParentsDto.builder()
                .telephone("010-3333-3333")
                .name("이엄마")
                .relation("모")
                .build();

        parentsDtoList2.add(father2);
        parentsDtoList2.add(mother2);

        ChildDto childDto2 = ChildDto.builder()
                .address(address)
                .childCode("A00002")
                .admissionDate(LocalDate.of(2024, 05, 01))
                .birthday(LocalDate.of(2019, 03, 01))
                .className(sunnyClass.getName())
                .status(true)
                .name("김어린이")
                .parentList(parentsDtoList2)
                .build();

        SunnyChildRideDto sunnyChildRideAm2 = SunnyChildRideDto.builder()
                .time("09:00")
                .comment("계단 옆")
                .sunnyRide(amRide)
                .build();

        SunnyChildRideDto sunnyChildRidePm2 = SunnyChildRideDto.builder()
                .time("16:00")
                .comment("아파트 뒷문")
                .sunnyRide(pmRide)
                .build();

        childDto2.setAmRide(sunnyChildRideAm2);
        childDto2.setPmRide(sunnyChildRidePm2);

        ChildDto child2 = childService.create(childDto2);
        if(child2 != null) log.info("원아생성");

    }
}
