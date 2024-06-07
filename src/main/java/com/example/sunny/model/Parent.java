package com.example.sunny.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sunny_parents")
public class Parent extends Person{
    @Column(name = "telephone")
    private String telephone;

    @Column(name = "relation")
    private String relation;

    @OneToMany(mappedBy = "parent")
    private List<ChildParent> childList;
}
