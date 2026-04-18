package com.sunny.model.dto;

import com.sunny.model.Parents;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ParentsDto {
    private Long id;

    @NotBlank(message = "보호자 이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "보호자 이름은 2자 이상 50자 이하로 입력해야 합니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Size(min = 10, max = 15, message = "전화번호는 10자 이상 15자 이하로 입력해야 합니다.")
    @Pattern(regexp = "^[0-9\\-]+$", message = "전화번호는 숫자와 하이픈만 포함할 수 있습니다.")
    private String telephone;

    @NotBlank(message = "관계는 필수입니다.")
    @Size(min = 2, max = 20, message = "관계는 2자 이상 20자 이하로 입력해야 합니다.")
    private String relation;

    @NotNull(message = "원아 ID는 필수입니다.")
    private Long childId;

    public ParentsDto(Parents parents) {
        this.id = parents.getId();
        this.name = parents.getName();
        this.telephone = parents.getTelephone();
        this.relation = parents.getRelation();
    }

    public Parents toEntity() {
        return Parents.builder()
                .relation(relation)
                .telephone(telephone)
                .id(id)
                .name(name)
                .build();
    }
}
