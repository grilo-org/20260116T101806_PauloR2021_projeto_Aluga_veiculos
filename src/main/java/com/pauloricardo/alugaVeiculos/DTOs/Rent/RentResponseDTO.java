package com.pauloricardo.alugaVeiculos.DTOs.Rent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RentResponseDTO(
        Integer id,
        Integer idUser,
        Integer idVehicle,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalValue,
        Boolean vehicleRemoved,
        Boolean vehicleReturned
) {
}
