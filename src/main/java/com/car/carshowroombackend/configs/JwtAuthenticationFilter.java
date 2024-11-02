package com.car.carshowroombackend.configs;

import com.car.carshowroombackend.services.jwt.UserService;
import com.car.carshowroombackend.utill.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter to validate JWT tokens for each request.
 * Extends OncePerRequestFilter to ensure a single execution per request.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;  // Utility for handling JWT operations
    private final UserService userService;  // Service to load user details

    /**
     * Filters incoming requests to validate JWT tokens in the Authorization header.
     *
     * @param request     HttpServletRequest containing client request data
     * @param response    HttpServletResponse containing server response data
     * @param filterChain FilterChain for executing further filters
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve Authorization header from the request
        final String authHeader = request.getHeader("Authorization");

        // If Authorization header is missing or doesn't start with "Bearer ", skip filtering
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token from the Authorization header
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtUtil.extractUserName(jwt);  // Extract username (email) from JWT

        // If email is not empty and no authentication exists in the security context
        if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details using the extracted email
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);

            // If JWT is valid, set the authentication in the security context
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                // Create authentication token for the user
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication token in the security context
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        // Continue the filter chain for the next request
        filterChain.doFilter(request, response);
    }
}
