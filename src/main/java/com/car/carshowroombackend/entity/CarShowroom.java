package com.car.carshowroombackend.entity;

import com.car.carshowroombackend.dto.CarShowroomDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class CarShowroom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_showroom_seq")
    @SequenceGenerator(name = "car_showroom_seq", sequenceName = "car_showroom_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    @Size(max = 100)
    private String name;

    @Column(name = "commercial_registration_number", nullable = false, unique = true, length = 10)
    @Pattern(regexp = "\\d{10}")
    private String commercialRegistrationNumber;

    @Column(name = "manager_name", length = 100)
    @Size(max = 100)
    private String managerName;

    @Column(name = "contact_number", nullable = false, length = 15)
    @Digits(integer = 15, fraction = 0)
    private String contactNumber;

    @Column(length = 255)
    @Size(max = 255)
    private String address;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public CarShowroomDTO getDto() {
        CarShowroomDTO dto = new CarShowroomDTO();

        dto.setId(id);
        dto.setName(name);
        dto.setAddress(address);
        dto.setManagerName(managerName);
        dto.setContactNumber(contactNumber);
        dto.setCommercialRegistrationNumber(commercialRegistrationNumber);
        dto.setUserId(user.getId());
        dto.setUserName(user.getName());

        return dto;
    }
}
