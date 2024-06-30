package com.example.sunny.model;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sunny_parents")
public class Parents extends Person{
    @Column(name = "telephone")
    private String telephone;

    @Column(name = "relation")
    private String relation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    @Setter
    private Child child;

    @Builder
    public Parents(Long id, String createdBy, String modifiedBy, String name, String telephone, String relation, Child child) {
        super(id, createdBy, modifiedBy, name);
        this.telephone = telephone;
        this.relation = relation;
        this.child = child;
    }
}
