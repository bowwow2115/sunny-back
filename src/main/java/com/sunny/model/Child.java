package com.sunny.model;

import com.sunny.model.embedded.Address;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Parents> parents = new ArrayList<>();
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChildMeetingLocation> childMeetingLocations = new ArrayList<>();

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

    public void updateChildRideList(List<ChildMeetingLocation> childMeetingLocationList) {
        this.childMeetingLocations = childMeetingLocationList;
    }

    public void updateParentList(List<Parents> parentList) {
        this.parents = parentList;
    }

    public void addParents(Parents parents) {
        this.parents.add(parents);
        parents.updateChild(this);
    }

    public void removeParents(Parents parents) {
        this.parents.remove(parents);
        parents.updateChild(null);
    }

    public void addRide(ChildMeetingLocation ride) {
        this.childMeetingLocations.add(ride);
        ride.updateChild(this);
    }

    public void removeRide(ChildMeetingLocation ride) {
        this.childMeetingLocations.remove(ride);
        ride.updateChild(null);
    }

}
