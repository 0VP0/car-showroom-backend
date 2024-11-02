package com.car.carshowroombackend.dto;

import com.car.carshowroombackend.enums.UserRole;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String email;
    private String name;

    private UserRole userRole;

    private Boolean enabled;
}
