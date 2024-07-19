package com.example.sunny.model.dto;

import com.example.sunny.model.Child;
import com.example.sunny.model.embedded.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChildDto {
    private Long id;
    private LocalDate admissionDate;
    private String className;
    private Address address;
    private LocalDate birthday;
    private boolean status;
    private String name;
    private ChildRideDto amRide;
    private ChildRideDto pmRide;
    private List<ParentsDto> parentList = new ArrayList<>();


    public ChildDto(Child child) {
        this.id = child.getId();
        this.birthday = child.getBirthday();
        this.admissionDate = child.getAdmissionDate();
        this.className = child.getClassName();
        this.address = child.getAddress();
        this.status = child.getStatus();
        this.name = child.getName();
    }

    public Child toEntity() {
        Child child = Child.builder()
                .id(id)
                .name(name)
                .status(status)
                .admissionDate(admissionDate)
                .address(address)
                .birthday(birthday)
                .className(className)
                .build();

        return child;
    }

}
