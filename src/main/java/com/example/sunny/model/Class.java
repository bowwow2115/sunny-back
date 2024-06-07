package com.example.sunny.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sunny_class")
public class Class extends BaseEntity{
    @Column(name = "className")
    private String className;
}
