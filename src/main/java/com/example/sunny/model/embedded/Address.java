package com.example.sunny.model.embedded;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    private String zipCode;
    private String address;
    private String detailAddress;
}
