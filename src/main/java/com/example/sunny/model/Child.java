package com.example.sunny.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sunny_children")
public class Child extends Person{
    @Column(name = "childCode")
    private String childCode;

    @Column(name = "admissionDate")
    private Date admissionDate;

    @Column(name = "class_name")
    private String className;

    @Column(name = "dong")
    private String dong;

    @Column(name = "city")
    private String city;

    @OneToMany(mappedBy = "child")
    private List<ChildParent> parentList;


}
