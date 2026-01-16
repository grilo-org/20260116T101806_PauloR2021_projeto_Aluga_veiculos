package com.pauloricardo.alugaVeiculos.DTOs.Payments;

import java.math.BigDecimal;

public record PaymentRequestDTO(
        Integer id_rent,
        BigDecimal amount
) {
}
