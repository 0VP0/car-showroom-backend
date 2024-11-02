package com.car.carshowroombackend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarDTO {

    private Long id;

    @NotBlank(message = "VIN is required")
    @Size(max = 25, message = "VIN must be at most 25 characters")
    private String vin;

    @NotBlank(message = "Maker is required")
    @Size(max = 25, message = "Maker must be at most 25 characters")
    private String maker;

    @NotBlank(message = "Model is required")
    @Size(max = 25, message = "Model must be at most 25 characters")
    private String model;

    @NotNull(message = "Model year is required")
    @Digits(integer = 4, fraction = 0, message = "Model year must be a 4-digit number")
    private Integer modelYear;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;


    private Long carShowroomId;

    private Long userId;

    private String userName;
    private String carShowroomName;
}
