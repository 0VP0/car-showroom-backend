package com.car.carshowroombackend.services.auth;

import com.car.carshowroombackend.dto.SignupRequest;
import com.car.carshowroombackend.dto.StatsDTO;
import com.car.carshowroombackend.dto.UserDTO;
import com.car.carshowroombackend.entity.User;
import com.car.carshowroombackend.enums.UserRole;
import com.car.carshowroombackend.repository.CarRepository;
import com.car.carshowroombackend.repository.CarShowroomRepository;
import com.car.carshowroombackend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for handling authentication and user management.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final CarShowroomRepository carShowroomRepository;

    /**
     * Initializes the service by creating an admin account if one does not already exist.
     * This method is executed after the bean is created due to the @PostConstruct annotation.
     */
    @PostConstruct
    public void createAnAdminAccount() {
        Optional<User> adminAccount = userRepository.findByUserRole(UserRole.ADMIN);

        // Check if an admin account exists; if not, create one
        if (adminAccount.isEmpty()) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("Admin");
            user.setUserRole(UserRole.ADMIN);
            user.setEnabled(true);
            user.setPassword(new BCryptPasswordEncoder().encode("admin")); // Encrypts the default admin password
            userRepository.save(user);
            System.out.println("Admin account created successfully");
        } else {
            System.out.println("Admin account already exists");
        }
    }

    /**
     * Creates a new user based on the provided signup request details.
     *
     * @param signupRequest Contains information needed to create a user, such as email, name, and password.
     * @return UserDto containing the details of the created user.
     * @throws EntityExistsException if a user with the same email already exists.
     */
    public UserDTO createUser(SignupRequest signupRequest) {
        // Check if a user with the same email already exists
        if (userRepository.findFirstByEmail(signupRequest.getEmail()).isPresent()) {
            throw new EntityExistsException("User Already Present With email " + signupRequest.getEmail());
        }

        // Create and save a new user
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setUserRole(UserRole.USER);
        user.setEnabled(true);
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        User createdUser = userRepository.save(user);

        return createdUser.getUserDto();
    }

    /**
     * Retrieves all users with a USER role and applies pagination.
     *
     * @param pageable Contains pagination settings.
     * @return Page of UserDto objects representing users with the USER role.
     */
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        // Fetch paginated users with the role USER
        Page<User> userPage = userRepository.findByUserRole(pageable, UserRole.USER);
        return userPage.map(User::getUserDto);
    }

    /**
     * Fetches statistics about users, cars, and showrooms.
     *
     * @return StatsDTO containing the counts of total and active users, cars, and showrooms.
     */
    public StatsDTO getStats() {
        StatsDTO statsDTO = new StatsDTO();

        // Get counts of total entities
        statsDTO.setTotalUsers(userRepository.count());
        statsDTO.setTotalCars(carRepository.count());
        statsDTO.setTotalShowrooms(carShowroomRepository.count());

        // Get counts of active (non-deleted) entities
        statsDTO.setActiveUsers(userRepository.countByEnabledTrue());
        statsDTO.setActiveCars(carRepository.countByIsDeletedFalse());
        statsDTO.setActiveShowrooms(carShowroomRepository.countByIsDeletedFalse());

        return statsDTO;
    }

    /**
     * Updates the status (enabled/disabled) of a user by user ID.
     *
     * @param id     ID of the user to update.
     * @param status New status for the user (true for enabled, false for disabled).
     * @return UserDto of the updated user.
     * @throws EntityNotFoundException if the user with the given ID is not found.
     */
    public UserDTO updateUserStatus(Long id, Boolean status) {
        Optional<User> optionalUser = userRepository.findById(id);

        // Check if user exists and update their status
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(status);
            return userRepository.save(user).getUserDto();
        } else {
            throw new EntityNotFoundException("User not found.");
        }
    }
}
