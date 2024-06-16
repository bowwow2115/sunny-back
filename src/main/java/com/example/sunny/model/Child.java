package com.example.sunny.model;

import com.example.sunny.model.embedded.Address;
import com.example.sunny.model.embedded.Ride;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_children")
public class Child extends Person{
    @Column(name = "child_code")
    private String childCode;
    @Column(name = "admission_date")
    private Date admissionDate;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "class_name")
    private String className;
    @Embedded
    private Address address;
    @OneToMany(mappedBy = "child")
    private List<ChildParents> parentList;
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

    @Column(name = "status")
    private boolean status;

    public boolean getStatus() {
        return status;
    }
    @Builder
    public Child(Long id, String name, String childCode, Date admissionDate, Date birthday, String className, Address address, List<ChildParents> parentList, Ride amRide, Ride pmRide, boolean status) {
        super(id, name);
        this.childCode = childCode;
        this.admissionDate = admissionDate;
        this.birthday = birthday;
        this.className = className;
        this.address = address;
        this.parentList = parentList;
        this.amRide = amRide;
        this.pmRide = pmRide;
        this.status = status;
    }
}
