package com.pauloricardo.alugaVeiculos.DTOs.Rent;

import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Models.Vehicles.VehicleModel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RentRequestDTO(


        Integer idUser,

        @NotNull(message = "Id Vehicle Is Required")
        Integer idVehicle,

        @NotNull(message = "Start Date Is Required")
        LocalDate startDate,

        @NotNull(message = "End Date Is Required")
        LocalDate endDate,

        Boolean vehicleRemoved,

        Boolean vehicleReturned
) {
}
