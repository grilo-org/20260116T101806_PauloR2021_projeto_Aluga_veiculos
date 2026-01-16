package com.pauloricardo.alugaVeiculos.DTOs.Vehicles;

import java.math.BigDecimal;

public record VehicleAdminUpdate(
        String mark,
        String model,
        String plate,
        Integer yearVehicle,
        BigDecimal dailyVelue,
        Boolean available

) {
}
