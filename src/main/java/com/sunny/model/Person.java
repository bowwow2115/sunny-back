package com.sunny.model;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@Getter
@Setter
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