package com.car.carshowroombackend.controller;


import com.car.carshowroombackend.dto.CarDTO;
import com.car.carshowroombackend.services.car.CarService;
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
 * Controller for managing car-related operations for users.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/car")  // Base URL for car-related endpoints
public class CarController {

    private final CarService carService;  // Service for car-related operations

    /**
     * Endpoint to create a new car.
     *
     * @param dto Car details for the new car
     * @return ResponseEntity containing the created car details or an error message
     */
    @PostMapping
    public ResponseEntity<?> createCar(@Valid @RequestBody CarDTO dto) {
        try {
            // Create the car using the provided details and return the created car
            return ResponseEntity.ok(carService.createCar(dto));
        } catch (EntityNotFoundException e) {
            // Handle case where the entity required to create the car is not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle any other exceptions during car creation
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to list cars with optional filtering.
     *
     * @param pageable        Pagination information
     * @param maker           Optional filter for car maker
     * @param carShowroomName Optional filter for showroom name
     * @param vin             Optional filter for VIN
     * @param modelYear       Optional filter for model year
     * @return ResponseEntity containing the list of cars or an error message
     */
    @GetMapping
    public ResponseEntity<?> listCars(
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @RequestParam(required = false) String maker,
            @RequestParam(required = false) String carShowroomName,
            @RequestParam(required = false) String vin,
            @RequestParam(required = false) Integer modelYear
    ) {
        try {
            // Retrieve the list of cars based on provided filters and pagination
            return ResponseEntity.ok(carService.listCars(pageable, maker, carShowroomName, vin, modelYear));
        } catch (EntityNotFoundException e) {
            // Handle case where the user associated with the request is not found
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle any other exceptions during listing cars
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to delete a car by its ID.
     *
     * @param id ID of the car to be deleted
     * @return ResponseEntity confirming deletion or an error message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        try {
            // Delete the car with the given ID and return the result
            return ResponseEntity.ok(carService.deleteCar(id));
        } catch (EntityNotFoundException e) {
            // Handle case where the car with the given ID is not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle any other exceptions during car deletion
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
