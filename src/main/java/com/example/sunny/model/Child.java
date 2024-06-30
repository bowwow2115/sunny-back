package com.example.sunny.model;

import com.example.sunny.model.embedded.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date admissionDate;
    @Column(name = "birthday", nullable = false)
    private Date birthday;
    @Column(name = "class_name")
    private String className;
    @Embedded
    private Address address;
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parents> parentList = new ArrayList<>();
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "name", column = @Column(name = "am_ride_name")),
//            @AttributeOverride(name = "time", column = @Column(name = "am_ride_time")),
//            @AttributeOverride(name = "comment", column = @Column(name = "am_ride_comment")),
//    })
//    private Ride amRide;
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "name", column = @Column(name = "pm_ride_name")),
//            @AttributeOverride(name = "time", column = @Column(name = "pm_ride_time")),
//            @AttributeOverride(name = "comment", column = @Column(name = "pm_ride_comment")),
//    })
//    private Ride pmRide;
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SunnyChildRide> sunnyChildRideList = new ArrayList<>();

    @Column(name = "status")
    private boolean status;

    public boolean getStatus() {
        return status;
    }

    //빌더 설정 시 parentsList는 add, remove 메소드로 관리하기 때문에 제외
    @Builder
    public Child(Long id, String createdBy, String modifiedBy, String name, String childCode, Date admissionDate, Date birthday, String className, Address address, boolean status) {
        super(id, createdBy, modifiedBy, name);
        this.childCode = childCode;
        this.admissionDate = admissionDate;
        this.birthday = birthday;
        this.className = className;
        this.address = address;
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

    public void addRide(SunnyChildRide ride) {
        this.sunnyChildRideList.add(ride);
        ride.setChild(this);
    }

    public void removeRide(SunnyChildRide ride) {
        this.sunnyChildRideList.remove(ride);
        ride.setChild(this);
    }

}
