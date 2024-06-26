package com.example.sunny.model.dto;

import com.example.sunny.model.Child;
import com.example.sunny.model.embedded.Address;
import com.example.sunny.model.embedded.Ride;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChildDto {
    private Long id;
    private String childCode;
    private Date admissionDate;
    private String className;
    private Address address;
    private Date birthday;
    private List<ParentsDto> parentList;
    private boolean status;
    private Ride amRide;
    private Ride pmRide;
    private String name;

    public ChildDto(Child child) {
        this.id = child.getId();
        this.birthday = child.getBirthday();
        this.childCode = child.getChildCode();
        this.admissionDate = child.getAdmissionDate();
        this.className = child.getClassName();
        this.address = child.getAddress();
        this.status = child.getStatus();
        this.amRide = child.getAmRide();
        this.pmRide = child.getPmRide();
        this.name = child.getName();
    }

    public Child toEntity() {
        return Child.builder()
                .id(id)
                .name(name)
                .status(status)
                .admissionDate(admissionDate)
                .amRide(amRide)
                .pmRide(pmRide)
                .address(address)
                .childCode(childCode)
                .birthday(birthday)
                .className(className)
                .build();
    }

}
