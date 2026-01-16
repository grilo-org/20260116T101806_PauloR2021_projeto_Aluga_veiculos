package com.pauloricardo.alugaVeiculos.DTOs.Vehicles;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record VehicleRequestDTO(
        @NotBlank(message = "Mark Is Required")
        @Size(max = 100,message = "Mark Is Size Max 100 Characters")
        String mark,

        @NotBlank(message = "Model Is Required")
        @Size(max = 100, message = "Model Is Size Max 100 Characters")
        String model,

        @NotBlank(message = "Plate Is Required")
        @Size(max = 10,message = "Plat Is Size Max 10 Characters")
        String plate,

        Integer yearVehicle,

        @NotNull(message = "Daily Value Is Required")
        @DecimalMin(value = "0.0",inclusive = false,message = "The Daily Rate Must Be Greater Than 0")
        BigDecimal dailyValue,

        Boolean available
) {
}
