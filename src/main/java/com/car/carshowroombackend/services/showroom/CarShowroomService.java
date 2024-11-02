package com.car.carshowroombackend.services.showroom;

import com.car.carshowroombackend.dto.CarShowroomDTO;
import com.car.carshowroombackend.dto.DropdownDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarShowroomService {

    CarShowroomDTO createCarShowroom(CarShowroomDTO dto);

    CarShowroomDTO updateCarShowroom(CarShowroomDTO dto, Long id);

    Page<CarShowroomDTO> listCarShowrooms(Pageable pageable);

    List<DropdownDTO> getCarShowroomsDropdown();

    CarShowroomDTO getCarShowroom(Long id);

    CarShowroomDTO deleteCarShowroom(Long id);
}
