package com.sunny.model.dto;

import com.sunny.model.Child;
import com.sunny.model.embedded.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChildDto {
    private Long id;

    @NotNull(message = "입학일은 필수입니다.")
    @PastOrPresent(message = "입학일은 현재 또는 과거 날짜여야 합니다.")
    private LocalDate admissionDate;

    @NotBlank(message = "반 이름은 필수입니다.")
    @Size(min = 1, max = 50, message = "반 이름은 1자 이상 50자 이하로 입력해야 합니다.")
    private String className;

    @Valid
    private Address address;

    @NotNull(message = "생년월일은 필수입니다.")
    @PastOrPresent(message = "생년월일은 현재 또는 과거 날짜여야 합니다.")
    private LocalDate birthday;

    @Size(max = 20, message = "상태는 20자 이하로 입력해야 합니다.")
    private String status;

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 1, max = 50, message = "이름은 1자 이상 50자 이하로 입력해야 합니다.")
    private String name;

    @Default
    private List<ChilMeetingLocationDto> childRideList = new ArrayList<>();
    @Default
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
