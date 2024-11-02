package com.car.carshowroombackend.configs;

import com.car.carshowroombackend.enums.UserRole;
import com.car.carshowroombackend.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Web Security Configuration class to define security settings for the application.
 * Configures HTTP security, authentication provider, and JWT filter.
 */
@Configuration
@EnableWebSecurity  // Enables Spring Security's web security support
@RequiredArgsConstructor  // Generates a constructor for dependency injection
public class WebSecurityConfiguration {

    private final UserService userService;  // Service for user details retrieval
    private final JwtAuthenticationFilter jwtAuthenticationFilter;  // JWT authentication filter

    /**
     * Configures the security filter chain for handling HTTP requests.
     *
     * @param http HttpSecurity object for configuring web-based security
     * @return Configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)  // Disable CSRF protection for stateless APIs
                .authorizeHttpRequests(request -> request
                        // Allow access to specific endpoints without authentication
                        .requestMatchers("/api/auth/**",
                                "/swagger-ui/**", "/v3/**", "/actuator/**",
                                "/actuator/cache").permitAll()
                        // Restrict access to admin endpoints to users with ADMIN role
                        .requestMatchers("/api/admin/**").hasAnyAuthority(UserRole.ADMIN.name())
                        // Restrict access to user endpoints to users with USER role
                        .requestMatchers("/api/user/**").hasAnyAuthority(UserRole.USER.name())
                        // All other requests require authentication
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))  // Set session management to stateless
                .authenticationProvider(authenticationProvider())  // Set the authentication provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before the username/password authentication filter

        return http.build();  // Build and return the security filter chain
    }

    /**
     * Provides an AuthenticationManager bean for managing authentication.
     *
     * @param config AuthenticationConfiguration object
     * @return Configured AuthenticationManager
     * @throws Exception if an error occurs during authentication manager creation
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();  // Retrieve and return the authentication manager
    }

    /**
     * Configures the authentication provider with user details service and password encoder.
     *
     * @return Configured AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());  // Set the user details service
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());  // Set the password encoder for hashing
        return authProvider;  // Return the configured authentication provider
    }
}
