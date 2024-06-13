package com.example.sunny.model.dto;

import com.example.sunny.model.ChildParents;
import com.example.sunny.model.Parents;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.List;

public class ParentsDto {
    private Long id;
    private String name;
    private String telephone;
    private String relation;
    private List<ChildParents> childList;

    public ParentsDto(Parents parents) {
        this.id = parents.getId();
        this.name = parents.getName();
        this.telephone = parents.getTelephone();
        this.relation = parents.getRelation();
        this.childList = parents.getChildList();
    }

    public Parents toEntity() {
        return Parents.builder()
                .childList(childList)
                .relation(relation)
                .telephone(telephone)
                .id(id)
                .name(name)
                .build();
    }
}
