package com.example.sunny.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "sunny_parents")
public class Parents extends Person{
    @Column(name = "telephone")
    private String telephone;

    @Column(name = "relation")
    private String relation;

    @OneToMany(mappedBy = "parents")
    private Set<ChildParents> childList;
}
