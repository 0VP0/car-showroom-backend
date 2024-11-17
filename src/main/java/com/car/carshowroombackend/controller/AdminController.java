package com.car.carshowroombackend.controller;

import com.car.carshowroombackend.services.auth.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling admin-related operations.
 * Provides endpoints for managing users and retrieving statistics.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")  // Base URL for all admin-related endpoints
public class AdminController {

    private final AuthService authService;  // Service for handling authentication-related tasks

    /**
     * Endpoint to retrieve all users with pagination support.
     *
     * @param pageable Contains pagination information (page size, sorting, etc.)
     * @return ResponseEntity containing a list of users or an error message
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            // Call the service to get all users and return them in the response
            return ResponseEntity.ok(authService.getAllUsers(pageable));
        } catch (EntityNotFoundException e) {
            // Handle case where no users are found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle any other exceptions and return an internal server error
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to retrieve statistics about users, cars, and showrooms.
     *
     * @return ResponseEntity containing statistics or an error message
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        try {
            // Call the service to get statistics and return them in the response
            return ResponseEntity.ok(authService.getStats());
        } catch (Exception e) {
            // Handle any exceptions and return an internal server error
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to update the status (enabled/disabled) of a user.
     *
     * @param id     ID of the user to be updated
     * @param status New status for the user (true for enabled, false for disabled)
     * @return ResponseEntity containing updated user information or an error message
     */
    @GetMapping("/update-user/{id}/{status}")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @PathVariable Boolean status) {
        try {
            // Call the service to update the user's status and return the result
            return ResponseEntity.ok(authService.updateUserStatus(id, status));
        } catch (EntityNotFoundException e) {
            // Handle case where the user to be updated is not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle any other exceptions and return an internal server error
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
