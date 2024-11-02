package com.car.carshowroombackend.controller;

import com.car.carshowroombackend.dto.CarShowroomDTO;
import com.car.carshowroombackend.services.showroom.CarShowroomService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing car showroom-related operations for users.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/car-showrooms")  // Base URL for car showroom-related endpoints
public class CarShowroomController {

    private final CarShowroomService carShowroomService;  // Service for car showroom-related operations

    /**
     * Endpoint to create a new car showroom.
     *
     * @param showroomDTO Details of the car showroom to be created
     * @return ResponseEntity containing the created showroom details or an error message
     */
    @PostMapping
    public ResponseEntity<?> createCarShowroom(@Valid @RequestBody CarShowroomDTO showroomDTO) {
        try {
            // Create the car showroom using the provided details and return the created showroom
            return ResponseEntity.ok(carShowroomService.createCarShowroom(showroomDTO));
        } catch (EntityNotFoundException e) {
            // Handle case where the user associated with the request is not found
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle any other exceptions during car showroom creation
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to update an existing car showroom by its ID.
     *
     * @param showroomDTO Updated details of the car showroom
     * @param id          ID of the showroom to be updated
     * @return ResponseEntity containing the updated showroom details or an error message
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarShowroom(@Valid @RequestBody CarShowroomDTO showroomDTO, @PathVariable Long id) {
        try {
            // Update the car showroom with the provided ID and return the updated showroom
            return ResponseEntity.ok(carShowroomService.updateCarShowroom(showroomDTO, id));
        } catch (EntityNotFoundException e) {
            // Handle case where the user associated with the request is not found
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle any other exceptions during car showroom update
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to list all car showrooms with pagination.
     *
     * @param pageable Pagination information
     * @return ResponseEntity containing the list of car showrooms or an error message
     */
    @GetMapping
    public ResponseEntity<?> listCarShowrooms(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        try {
            // Retrieve the list of car showrooms based on pagination
            return ResponseEntity.ok(carShowroomService.listCarShowrooms(pageable));
        } catch (EntityNotFoundException e) {
            // Handle case where the user associated with the request is not found
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle any other exceptions during listing car showrooms
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to get a dropdown list of all car showrooms.
     *
     * @return ResponseEntity containing the dropdown list of car showrooms or an error message
     */
    @GetMapping("/all")
    public ResponseEntity<?> getCarShowroomsDropdown() {
        try {
            // Retrieve a list of car showrooms for dropdown selection
            return ResponseEntity.ok(carShowroomService.getCarShowroomsDropdown());
        } catch (EntityNotFoundException e) {
            // Handle case where the user associated with the request is not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle any other exceptions during retrieving dropdown list
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to get a specific car showroom by its ID.
     *
     * @param id ID of the showroom to be retrieved
     * @return ResponseEntity containing the requested showroom details or an error message
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCarShowroom(@PathVariable Long id) {
        try {
            // Retrieve the car showroom with the given ID and return its details
            return ResponseEntity.ok(carShowroomService.getCarShowroom(id));
        } catch (EntityNotFoundException e) {
            // Handle case where the car showroom with the given ID is not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle any other exceptions during retrieving the showroom
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to delete a car showroom by its ID.
     *
     * @param id ID of the showroom to be deleted
     * @return ResponseEntity confirming deletion or an error message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarShowroom(@PathVariable Long id) {
        try {
            // Delete the car showroom with the given ID and return the result
            return ResponseEntity.ok(carShowroomService.deleteCarShowroom(id));
        } catch (EntityNotFoundException e) {
            // Handle case where the car showroom with the given ID is not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle any other exceptions during showroom deletion
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
