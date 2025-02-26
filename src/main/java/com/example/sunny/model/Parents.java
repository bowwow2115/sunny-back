package com.example.sunny.model;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sunny_parents")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Parents extends Person{
    @Column(name = "telephone")
    @EqualsAndHashCode.Include
    private String telephone;

    @Column(name = "relation")
    @EqualsAndHashCode.Include
    private String relation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", updatable = false)
    @EqualsAndHashCode.Include
    private Child child;

    @Builder
    public Parents(Long id, String createdBy, String modifiedBy, String name, String telephone, String relation, Child child) {
        super(id, createdBy, modifiedBy, name);
        this.telephone = telephone;
        this.relation = relation;
        this.child = child;
    }

    public void addChild(Child child) {
        this.child = child;
        child.addParents(this);
    }

    public void updateChild(Child child) {
        this.child = child;
    }
}
