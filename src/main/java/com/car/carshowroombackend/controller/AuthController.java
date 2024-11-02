package com.car.carshowroombackend.controller;

import com.car.carshowroombackend.dto.AuthenticationRequest;
import com.car.carshowroombackend.dto.AuthenticationResponse;
import com.car.carshowroombackend.dto.SignupRequest;
import com.car.carshowroombackend.dto.UserDTO;
import com.car.carshowroombackend.entity.User;
import com.car.carshowroombackend.repository.UserRepository;
import com.car.carshowroombackend.services.auth.AuthService;
import com.car.carshowroombackend.services.jwt.UserService;
import com.car.carshowroombackend.utill.JwtUtil;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controller for handling authentication operations such as user signup and login.
 */
@RestController
@RequestMapping("/api/auth")  // Base URL for authentication-related endpoints
@RequiredArgsConstructor  // Automatically generates a constructor with required arguments
public class AuthController {

    private final AuthService authService;  // Service for authentication-related tasks

    private final AuthenticationManager authenticationManager;  // Manages authentication process

    private final UserRepository userRepository;  // Repository for accessing user data

    private final JwtUtil jwtUtil;  // Utility for generating and validating JWT tokens

    private final UserService userService;  // Service for loading user details

    /**
     * Endpoint for user signup.
     *
     * @param signupRequest Contains user details for signup
     * @return ResponseEntity containing the created user details or an error message
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        try {
            // Create a new user and return the user details
            UserDTO createdUser = authService.createUser(signupRequest);
            return new ResponseEntity<>(createdUser, HttpStatus.OK);
        } catch (EntityExistsException entityExistsException) {
            // Handle case where the user already exists
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            // Handle any other exceptions during signup
            return new ResponseEntity<>("User not created, come again later", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint for user login.
     *
     * @param authenticationRequest Contains user credentials for authentication
     * @return ResponseEntity containing the JWT token and user details or an error message
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            // Authenticate the user based on provided credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            // Handle case where credentials are incorrect
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (DisabledException e) {
            // Handle case where the user account is disabled
            return new ResponseEntity<>(e.getMessage(), HttpStatus.LOCKED);
        }

        // Load user details and generate a JWT token
        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        // Prepare the authentication response with JWT and user information
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
            authenticationResponse.setUserId(optionalUser.get().getId());
        }
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }
}
