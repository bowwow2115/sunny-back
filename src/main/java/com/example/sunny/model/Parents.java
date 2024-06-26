package com.example.sunny.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_parents")
public class Parents extends Person{
    @Column(name = "telephone")
    private String telephone;

    @Column(name = "relation")
    private String relation;

    @OneToMany(mappedBy = "parents", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChildParents> childList;

    @Builder
    public Parents(Long id, String name, String telephone, String relation, List<ChildParents> childList) {
        super(id, name);
        this.telephone = telephone;
        this.relation = relation;
        this.childList = childList;
    }
}
