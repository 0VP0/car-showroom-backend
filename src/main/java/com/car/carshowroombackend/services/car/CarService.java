package com.car.carshowroombackend.services.car;


import com.car.carshowroombackend.dto.CarDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarService {

    CarDTO createCar(CarDTO dto);

    Page<CarDTO> listCars(Pageable pageable, String maker, String carShowroomName, String vid, Integer modelYear);

    CarDTO deleteCar(Long id);
}
