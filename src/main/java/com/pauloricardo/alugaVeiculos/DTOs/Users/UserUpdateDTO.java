package com.pauloricardo.alugaVeiculos.DTOs.Users;

import com.pauloricardo.alugaVeiculos.Models.Users.UserRole;

public record UserUpdateDTO(
        String name,
        String password,
        UserRole role
) {
}
