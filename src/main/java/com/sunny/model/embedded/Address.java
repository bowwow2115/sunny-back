package com.sunny.model.embedded;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    @Size(max = 10, message = "우편번호는 최대 10자까지 입력할 수 있습니다.")
    private String zipCode;

    @Size(max = 200, message = "주소는 최대 200자까지 입력할 수 있습니다.")
    private String address;

    @Size(max = 200, message = "상세주소는 최대 200자까지 입력할 수 있습니다.")
    private String detailAddress;
}
