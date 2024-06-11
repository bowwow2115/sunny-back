package com.example.sunny.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Person extends BaseEntity {

    public Person(Long id, String name) {
        super(id);
        this.name = name;
    }

    @Column(name = "name")
    private String name;

}