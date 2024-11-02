package com.car.carshowroombackend.services.car;

import com.car.carshowroombackend.dto.CarDTO;
import com.car.carshowroombackend.entity.Car;
import com.car.carshowroombackend.entity.CarShowroom;
import com.car.carshowroombackend.entity.User;
import com.car.carshowroombackend.repository.CarRepository;
import com.car.carshowroombackend.repository.CarShowroomRepository;
import com.car.carshowroombackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Car related operations.
 */
@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarShowroomRepository showroomRepository;

    private final UserRepository userRepository;

    private final CarRepository carRepository;

    /**
     * Creates a new Car entity based on the provided CarDTO.
     *
     * @param dto CarDTO containing the details of the car to create.
     * @return CarDTO of the created car.
     */
    @Transactional
    public CarDTO createCar(CarDTO dto) {
        Car car = new Car();
        car.setModel(dto.getModel());
        car.setMaker(dto.getMaker());
        car.setModelYear(dto.getModelYear());
        car.setVin(dto.getVin());
        car.setDeleted(false); // Initially set deleted flag to false
        car.setPrice(dto.getPrice());

        // Retrieve and set the User associated with this car
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        car.setUser(user);

        // Retrieve and set the CarShowroom associated with this car
        CarShowroom carShowroom = showroomRepository.findById(dto.getCarShowroomId())
                .orElseThrow(() -> new IllegalArgumentException("Showroom not found"));
        car.setCarShowroom(carShowroom);

        return carRepository.save(car).getDto();
    }

    /**
     * Lists cars with optional filtering and pagination.
     *
     * @param pageable        Pageable object for pagination settings.
     * @param maker           Filter by car maker.
     * @param carShowroomName Filter by car showroom name.
     * @param vin             Filter by VIN (Vehicle Identification Number).
     * @param modelYear       Filter by model year.
     * @return Page of CarDTOs matching the filter criteria.
     */
    public Page<CarDTO> listCars(Pageable pageable, String maker, String carShowroomName, String vin, Integer modelYear) {

        // Create specifications for filtering the cars based on provided criteria
        Specification<Car> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter out deleted cars and showrooms
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            predicates.add(criteriaBuilder.equal(root.get("carShowroom").get("isDeleted"), false));

            // Apply filters for each provided criterion
            if (maker != null && !maker.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("maker"), maker));
            }
            if (vin != null && !vin.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("vin"), vin));
            }
            if (modelYear != null) {
                predicates.add(criteriaBuilder.equal(root.get("modelYear"), modelYear));
            }
            if (carShowroomName != null && !carShowroomName.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("carShowroom").get("name"), carShowroomName));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Fetch paginated list of cars matching the specification
        Page<Car> carPage = carRepository.findAll(spec, pageable);
        return carPage.map(Car::getDto);
    }

    /**
     * Soft deletes a car by setting the 'deleted' flag to true.
     *
     * @param id ID of the car to delete.
     * @return CarDTO of the deleted car.
     * @throws EntityNotFoundException if the car is not found.
     */
    public CarDTO deleteCar(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            car.setDeleted(true); // Soft delete by setting deleted flag
            return carRepository.save(car).getDto();
        } else {
            throw new EntityNotFoundException("Car not present.");
        }
    }
}
