package com.pauloricardo.alugaVeiculos.DTOs.Rent;

import com.pauloricardo.alugaVeiculos.Models.Users.UserRole;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RentResponseQueryUserVehicleDTO(
        Integer rentId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalValue,
        Boolean vehicleRemoved,
        Boolean vehicleReturned,


        Integer userId,
        String username,
        String nameUser,
        UserRole role,

        Integer vehicleId,
        String mark,
        String model,
        String plate,
        Integer yearVehicle,
        BigDecimal dailyValue,
        Boolean available

) {
}
