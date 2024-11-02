package com.car.carshowroombackend.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CarShowroomDTO {

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Pattern(regexp = "\\d{10}")
    private String commercialRegistrationNumber;

    @Size(max = 100)
    private String managerName;

    @NotBlank
    @Digits(integer = 15, fraction = 0)
    private String contactNumber;

    @Size(max = 255)
    private String address;

    private Long userId;

    private String userName;

}
