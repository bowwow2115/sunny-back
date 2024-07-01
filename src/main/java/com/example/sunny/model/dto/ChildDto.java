package com.example.sunny.model.dto;

import com.example.sunny.model.Child;
import com.example.sunny.model.embedded.Address;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChildDto {
    private Long id;
    private String childCode;
    private LocalDateTime admissionDate;
    private String className;
    private Address address;
    private LocalDateTime birthday;
    private List<ParentsDto> parentList = new ArrayList<>();
    private boolean status;
    private SunnyChildRideDto amRide;
    private SunnyChildRideDto pmRide;
    private String name;

    public ChildDto(Child child) {
        this.id = child.getId();
        this.birthday = child.getBirthday();
        this.childCode = child.getChildCode();
        this.admissionDate = child.getAdmissionDate();
        this.className = child.getClassName();
        this.address = child.getAddress();
        this.status = child.getStatus();
        this.name = child.getName();
        //childRide 할당
        if(child.getSunnyChildRideList() != null && child.getSunnyChildRideList().size() != 0) {
            child.getSunnyChildRideList().stream()
                    .filter((item) -> item.getSunnyRide().isAm())
                    .findAny()
                    .ifPresent((item) -> {
                        this.amRide = SunnyChildRideDto.builder()
                                .comment(item.getComment())
                                .sunnyRide(new SunnyRideDto(item.getSunnyRide()))
                                .time(item.getTime())
                                .id(item.getId())
                                .build();
                    });

            child.getSunnyChildRideList().stream()
                    .filter((item) -> !item.getSunnyRide().isAm())
                    .findAny()
                    .ifPresent((item) -> {
                        this.pmRide = SunnyChildRideDto.builder()
                                .comment(item.getComment())
                                .sunnyRide(new SunnyRideDto(item.getSunnyRide()))
                                .time(item.getTime())
                                .id(item.getId())
                                .build();
                    });
        }
        //parents 할당
        if(child.getParentList() != null && child.getParentList().size() != 0)
            this.parentList = child.getParentList().stream().map(ParentsDto::new).collect(Collectors.toList());
    }

    public Child toEntity() {
        Child child = Child.builder()
                .id(id)
                .name(name)
                .status(status)
                .admissionDate(admissionDate)
                .address(address)
                .childCode(childCode)
                .birthday(birthday)
                .className(className)
                .build();
        if(this.parentList.size() != 0)
            for(ParentsDto parentsDto : this.parentList) {
                child.addParents(parentsDto.toEntity());
            }
        return child;
    }

}
