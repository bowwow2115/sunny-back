package com.example.sunny.model.dto;

import com.example.sunny.model.Parents;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ParentsDto {
    private Long id;
    private String name;
    private String telephone;
    private String relation;
    private ChildDto childDto;
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
