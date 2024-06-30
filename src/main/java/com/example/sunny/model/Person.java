package com.example.sunny.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Person extends BaseEntity {

    public Person(Long id, String createdBy, String modifiedBy, String name) {
        super(id, createdBy, modifiedBy);
        this.name = name;
    }

    @Column(name = "name")
    private String name;

}