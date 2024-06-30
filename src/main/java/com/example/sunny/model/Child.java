package com.example.sunny.model;

import com.example.sunny.model.embedded.Address;
import com.example.sunny.model.embedded.Ride;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_children")
public class Child extends Person{
    @Column(name = "child_code",unique = true, nullable = false)
    private String childCode;
    @Column(name = "admission_date", nullable = false)
    private Date admissionDate;
    @Column(name = "birthday", nullable = false)
    private Date birthday;
    @Column(name = "class_name")
    private String className;
    @Embedded
    private Address address;
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parents> parentList = new ArrayList<>();
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
    public Child(Long id, String name, String childCode, Date admissionDate, Date birthday, String className, Address address, Ride amRide, Ride pmRide, boolean status) {
        super(id, name);
        this.childCode = childCode;
        this.admissionDate = admissionDate;
        this.birthday = birthday;
        this.className = className;
        this.address = address;
        this.amRide = amRide;
        this.pmRide = pmRide;
        this.status = status;
    }

    public void addParents(Parents parents) {
        this.parentList.add(parents);
        parents.setChild(this);
    }

    public void removeParents(Parents parents) {
        this.parentList.remove(parents);
        parents.setChild(null);
    }
}
