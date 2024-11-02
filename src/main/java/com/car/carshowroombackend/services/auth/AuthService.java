package com.car.carshowroombackend.services.auth;


import com.car.carshowroombackend.dto.SignupRequest;
import com.car.carshowroombackend.dto.StatsDTO;
import com.car.carshowroombackend.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthService {

    UserDTO createUser(SignupRequest signupRequest);

    Page<UserDTO> getAllUsers(Pageable pageable);

    StatsDTO getStats();

    UserDTO updateUserStatus(Long id, Boolean status);
}
