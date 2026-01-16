package com.pauloricardo.alugaVeiculos.DTOs.Vehicles;

import java.math.BigDecimal;

public record VehicleResponseDTO(
        Integer id,
        String mark,
        String model,
        String plate,
        Integer yearVehicle,
        BigDecimal dailyValue,
        Boolean available

) {

}



