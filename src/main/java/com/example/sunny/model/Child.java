package com.example.sunny.model;

import com.example.sunny.model.embedded.Address;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sunny_children")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
//@NamedEntityGraphs(
//        @NamedEntityGraph(
//        name = "Child.withAll",
//        attributeNodes = {
//                @NamedAttributeNode(value = "childRideList", subgraph = "childRideGraph"),
//                @NamedAttributeNode(value = "parentList"),},
//        subgraphs = {
//                @NamedSubgraph(
//                        name = "childRideGraph",
//                        attributeNodes = {
//                                @NamedAttributeNode(value = "meetingLocation", subgraph = "meetingLocationGraph")
//                        }),
//                @NamedSubgraph(
//                        name = "meetingLocationGraph",
//                        attributeNodes = {
//                                @NamedAttributeNode("sunnyRide")
//                        }),
//        })
//)
public class Child extends Person{
    @Column(name = "admission_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @EqualsAndHashCode.Include
    private LocalDate admissionDate;
    @Column(name = "birthday", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @EqualsAndHashCode.Include
    private LocalDate birthday;
    @Column(name = "class_name")
    @EqualsAndHashCode.Include
    private String className;
    @Embedded
    private Address address;
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parents> parentList = new ArrayList<>();
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChildRide> childRideList = new ArrayList<>();

    @Column(name = "status")
    @EqualsAndHashCode.Include
    private String status;

    //빌더 설정 시 parentsList는 add, remove 메소드로 관리하기 때문에 제외
    @Builder
    public Child(Long id, String createdBy, String modifiedBy, String name, LocalDate admissionDate, LocalDate birthday, String className, Address address, String status) {
        super(id, createdBy, modifiedBy, name);
        this.admissionDate = admissionDate;
        this.birthday = birthday;
        this.className = className;
        this.address = address;
        this.status = status;
    }

    public void updateClassName(String className) {
        this.className = className;
    }

    public void updateChildRideList(List<ChildRide> childRideList) {
        this.childRideList = childRideList;
    }

    public void updateParentList(List<Parents> parentList) {
        this.parentList = parentList;
    }

    public void addParents(Parents parents) {
        this.parentList.add(parents);
        parents.updateChild(this);
    }

    public void removeParents(Parents parents) {
        this.parentList.remove(parents);
        parents.updateChild(null);
    }

    public void addRide(ChildRide ride) {
        this.childRideList.add(ride);
        ride.updateChild(this);
    }

    public void removeRide(ChildRide ride) {
        this.childRideList.remove(ride);
        ride.updateChild(null);
    }

}
