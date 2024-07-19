package com.example.sunny.model;

import com.example.sunny.model.embedded.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_children")
public class Child extends Person{
    @Column(name = "admission_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate admissionDate;
    @Column(name = "birthday", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @Column(name = "class_name")
    private String className;
    @Embedded
    private Address address;
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private List<Parents> parentList = new ArrayList<>();
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChildRide> childRideList = new ArrayList<>();

    @Column(name = "status")
    private boolean status;

    public boolean getStatus() {
        return status;
    }

    //빌더 설정 시 parentsList는 add, remove 메소드로 관리하기 때문에 제외
    @Builder
    public Child(Long id, String createdBy, String modifiedBy, String name, LocalDate admissionDate, LocalDate birthday, String className, Address address, boolean status) {
        super(id, createdBy, modifiedBy, name);
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

    public void addRide(ChildRide ride) {
        this.childRideList.add(ride);
        ride.setChild(this);
    }

    public void removeRide(ChildRide ride) {
        this.childRideList.remove(ride);
        ride.setChild(null);
    }

}
