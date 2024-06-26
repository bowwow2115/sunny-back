package com.example.sunny.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChildParentsDto {
    private Long id;
    private ChildDto child;
    private ParentsDto parents;
}
