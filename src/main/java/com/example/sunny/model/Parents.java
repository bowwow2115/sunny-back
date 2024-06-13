package com.example.sunny.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_parents")
public class Parents extends Person{
    @Column(name = "telephone")
    private String telephone;

    @Column(name = "relation")
    private String relation;

    @OneToMany(mappedBy = "parents")
    private List<ChildParents> childList;

    @Builder
    public Parents(Long id, String name, String telephone, String relation, List<ChildParents> childList) {
        super(id, name);
        this.telephone = telephone;
        this.relation = relation;
        this.childList = childList;
    }
}
