package com.pauloricardo.alugaVeiculos.DTOs.Payments;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseTotalDTO(
        Integer paymentId,
        Integer rentId,
        BigDecimal amount,
        String method,
        String status,
        LocalDateTime paymentDate
) {
}
