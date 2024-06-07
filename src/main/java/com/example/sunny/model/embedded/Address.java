package com.example.sunny.model.embedded;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Address {
    private String address;
    private String detailAddress;
}
