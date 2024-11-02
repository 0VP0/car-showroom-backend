package com.car.carshowroombackend.repository;

import com.car.carshowroombackend.entity.User;
import com.car.carshowroombackend.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);

    Optional<User> findByUserRole(UserRole userRole);

    Page<User> findByUserRole(Pageable pageable, UserRole userRole);

    Long countByEnabledTrue();
}
