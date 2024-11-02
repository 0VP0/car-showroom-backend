package com.car.carshowroombackend.dto;

import com.car.carshowroombackend.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String jwt;

    private Long userId;

    private UserRole userRole;
}
