package com.example.sunny.model.dto;

import com.example.sunny.model.Child;
import com.example.sunny.model.embedded.Address;
import com.example.sunny.model.embedded.Ride;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
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
    private Date admissionDate;
    private String className;
    private Address address;
    private Date birthday;
    private List<ParentsDto> parentList = new ArrayList<>();
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
        if(child.getParentList() != null && child.getParentList().size() != 0)
            this.parentList = child.getParentList().stream().map(ParentsDto::new).collect(Collectors.toList());
    }

    public Child toEntity() {
        Child child = Child.builder()
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
        if(this.parentList.size() != 0)
            for(ParentsDto parentsDto : this.parentList) {
                child.addParents(parentsDto.toEntity());
            }
        return child;
    }

    public void addParents(ParentsDto parentsDto) {
        this.parentList.add(parentsDto);
        parentsDto.setChildDto(this);
    }

    public void removeParents(ParentsDto parentsDto) {
        this.parentList.remove(parentsDto);
        parentsDto.setChildDto(null);
    }

}
