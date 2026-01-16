package com.pauloricardo.alugaVeiculos.DTOs.Rent;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RentRequestUpdateDTO(
        Integer idUser,

        @NotNull(message = "Start Date Is Required")
        LocalDate startDate,

        @NotNull(message = "End Date Is Required")
        LocalDate endDate,

        Boolean vehicleRemoved,

        Boolean vehicleReturned
) {

}
