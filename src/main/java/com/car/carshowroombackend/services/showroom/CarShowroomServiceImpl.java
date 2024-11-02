package com.car.carshowroombackend.services.showroom;

import com.car.carshowroombackend.dto.CarShowroomDTO;
import com.car.carshowroombackend.dto.DropdownDTO;
import com.car.carshowroombackend.entity.CarShowroom;
import com.car.carshowroombackend.entity.User;
import com.car.carshowroombackend.repository.CarShowroomRepository;
import com.car.carshowroombackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for CarShowroom related operations.
 */
@AllArgsConstructor
@Service
public class CarShowroomServiceImpl implements CarShowroomService {

    private final CarShowroomRepository showroomRepository;

    private final UserRepository userRepository;

    /**
     * Creates a new CarShowroom based on the provided DTO.
     *
     * @param dto CarShowroomDTO containing the details of the showroom to create.
     * @return CarShowroomDTO of the created showroom.
     */
    @Transactional
    public CarShowroomDTO createCarShowroom(CarShowroomDTO dto) {
        CarShowroom showroom = new CarShowroom();
        showroom.setName(dto.getName());
        showroom.setCommercialRegistrationNumber(dto.getCommercialRegistrationNumber());
        showroom.setManagerName(dto.getManagerName());
        showroom.setContactNumber(dto.getContactNumber());
        showroom.setAddress(dto.getAddress());
        showroom.setDeleted(false);

        // Retrieve and set the User associated with this showroom
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        showroom.setUser(user);

        return showroomRepository.save(showroom).getDto();
    }

    /**
     * Lists all non-deleted CarShowrooms with pagination support.
     *
     * @param pageable Pagination information.
     * @return Page containing CarShowroomDTOs of available showrooms.
     */
    public Page<CarShowroomDTO> listCarShowrooms(Pageable pageable) {
        Page<CarShowroom> showroomPage = showroomRepository.findAllByIsDeletedFalse(pageable);
        return showroomPage.map(CarShowroom::getDto);
    }

    /**
     * Retrieves a list of CarShowrooms for dropdown selection.
     *
     * @return List of DropdownDTO representing non-deleted car showrooms.
     */
    public List<DropdownDTO> getCarShowroomsDropdown() {
        List<CarShowroom> carShowrooms = showroomRepository.findAllByIsDeletedFalse();
        return carShowrooms.stream().map(carShowroom -> {
            DropdownDTO dropdownDTO = new DropdownDTO();
            dropdownDTO.setId(carShowroom.getId());
            dropdownDTO.setName(carShowroom.getName());
            return dropdownDTO;
        }).collect(Collectors.toList());
    }

    /**
     * Retrieves a CarShowroom by its ID with caching enabled.
     *
     * @param id ID of the CarShowroom to retrieve.
     * @return CarShowroomDTO of the requested showroom.
     * @throws EntityNotFoundException if the showroom is not found.
     */
    @Cacheable(value = "carShowrooms", key = "#id")
    public CarShowroomDTO getCarShowroom(Long id) {
        Optional<CarShowroom> optionalCarShowroom = showroomRepository.findById(id);
        if (optionalCarShowroom.isPresent()) {
            return optionalCarShowroom.get().getDto();
        } else {
            throw new EntityNotFoundException("Showroom not present.");
        }
    }

    /**
     * Marks a CarShowroom as deleted by setting the 'deleted' flag.
     *
     * @param id ID of the CarShowroom to delete.
     * @return CarShowroomDTO of the deleted showroom.
     * @throws EntityNotFoundException if the showroom is not found.
     */
    public CarShowroomDTO deleteCarShowroom(Long id) {
        Optional<CarShowroom> optionalCarShowroom = showroomRepository.findById(id);
        if (optionalCarShowroom.isPresent()) {
            CarShowroom showroom = optionalCarShowroom.get();
            showroom.setDeleted(true);
            return showroomRepository.save(showroom).getDto();
        } else {
            throw new EntityNotFoundException("Showroom not present.");
        }
    }

    /**
     * Updates an existing CarShowroom with new details from the provided DTO.
     *
     * @param dto CarShowroomDTO containing updated details of the showroom.
     * @param id  ID of the CarShowroom to update.
     * @return CarShowroomDTO of the updated showroom.
     * @throws EntityNotFoundException if the showroom is not found.
     */
    @Transactional
    public CarShowroomDTO updateCarShowroom(CarShowroomDTO dto, Long id) {
        Optional<CarShowroom> optionalCarShowroom = showroomRepository.findById(id);
        if (optionalCarShowroom.isPresent()) {
            CarShowroom showroom = optionalCarShowroom.get();
            showroom.setName(dto.getName());
            showroom.setCommercialRegistrationNumber(dto.getCommercialRegistrationNumber());
            showroom.setManagerName(dto.getManagerName());
            showroom.setContactNumber(dto.getContactNumber());
            showroom.setAddress(dto.getAddress());

            return showroomRepository.save(showroom).getDto();
        } else {
            throw new EntityNotFoundException("Showroom not present.");
        }
    }
}
