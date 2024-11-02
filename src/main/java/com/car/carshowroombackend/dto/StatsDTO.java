package com.car.carshowroombackend.dto;

import lombok.Data;

@Data
public class StatsDTO {

    private Long totalUsers;
    private Long activeUsers;

    private Long totalShowrooms;
    private Long activeShowrooms;

    private Long totalCars;
    private Long activeCars;
}
