package com.example.sunny.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Person extends BaseEntity {

    public Person(Long id, String createdBy, String modifiedBy, String name) {
        super(id, createdBy, modifiedBy);
        this.name = name;
    }

    @Column(name = "name")
    @EqualsAndHashCode.Include
    private String name;

}