package com.pauloricardo.alugaVeiculos.DTOs.Users;

import com.pauloricardo.alugaVeiculos.Models.Users.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotBlank
        String name,

        @NotBlank
        String username,

        @NotBlank
        String password,

        @NotNull(message = "Role cannot be null")
        UserRole role
) {
}
