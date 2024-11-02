package com.car.carshowroombackend.repository;

import com.car.carshowroombackend.entity.CarShowroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarShowroomRepository extends JpaRepository<CarShowroom, Long> {

    List<CarShowroom> findAllByIsDeletedFalse();

    Page<CarShowroom> findAllByIsDeletedFalse(Pageable pageable);

    Long countByIsDeletedFalse();
}
