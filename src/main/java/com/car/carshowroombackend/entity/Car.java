package com.car.carshowroombackend.entity;

import com.car.carshowroombackend.dto.CarDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 25)
    @NotNull
    @Size(max = 25)
    private String vin;

    @Column(nullable = false, length = 25)
    @NotNull
    @Size(max = 25)
    private String maker;

    @Column(nullable = false, length = 25)
    @NotNull
    @Size(max = 25)
    private String model;

    @Column(name = "model_year", nullable = false)
    @NotNull
    @Digits(integer = 4, fraction = 0)
    private Integer modelYear;

    @Column(nullable = false)
    @NotNull
    private BigDecimal price;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "car_showroom_id", nullable = false)
    private CarShowroom carShowroom;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public CarDTO getDto() {
        CarDTO dto = new CarDTO();
        dto.setId(id);
        dto.setVin(vin);
        dto.setMaker(maker);
        dto.setModel(model);
        dto.setModelYear(modelYear);
        dto.setPrice(price);
        dto.setCarShowroomId(carShowroom != null ? carShowroom.getId() : null);
        dto.setCarShowroomName(carShowroom != null ? carShowroom.getName() : null);
        dto.setUserId(user != null ? user.getId() : null);
        dto.setUserName(user != null ? user.getName() : null);
        return dto;
    }
}
