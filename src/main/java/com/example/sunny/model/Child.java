package com.example.sunny.model;

import com.example.sunny.model.embedded.Address;
import com.example.sunny.model.embedded.Ride;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sunny_children")
public class Child extends Person{
    @Column(name = "child_code")
    private String childCode;

    @Column(name = "admission_date")
    private Date admissionDate;

    @Column(name = "class_name")
    private String className;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "child")
    private Set<ChildParents> parentList;

    @Column(name = "status")
    private String status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "am_ride_name")),
            @AttributeOverride(name = "time", column = @Column(name = "am_ride_time")),
            @AttributeOverride(name = "comment", column = @Column(name = "am_ride_comment")),
    })
    private Ride amRide;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "pm_ride_name")),
            @AttributeOverride(name = "time", column = @Column(name = "pm_ride_time")),
            @AttributeOverride(name = "comment", column = @Column(name = "pm_ride_comment")),
    })
    private Ride pmRide;

}
