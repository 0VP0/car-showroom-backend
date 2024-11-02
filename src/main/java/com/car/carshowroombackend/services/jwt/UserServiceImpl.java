package com.car.carshowroombackend.services.jwt;

import com.car.carshowroombackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling user-related operations, specifically
 * for loading user details required by Spring Security for authentication.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Provides an implementation of the UserDetailsService, which is responsible for
     * retrieving user information from the repository based on the username (email).
     *
     * @return UserDetailsService that loads user details from the database.
     */
    @Override
    public UserDetailsService userDetailsService() {
        // Returns a UserDetailsService that retrieves a user's details by their username (email)
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // Searches for the user in the repository by email. Throws an exception if not found.
                return userRepository.findFirstByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
}
