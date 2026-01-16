package com.pauloricardo.alugaVeiculos.DTOs.Users;

import com.pauloricardo.alugaVeiculos.Models.Users.UserRole;

import java.math.BigDecimal;

public record UserResponseDTO(
        Integer id,
        String name,
        String user,
        UserRole role
) {

}
