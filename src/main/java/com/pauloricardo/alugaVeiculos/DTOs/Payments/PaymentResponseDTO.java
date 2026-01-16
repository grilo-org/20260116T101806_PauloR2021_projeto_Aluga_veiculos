package com.pauloricardo.alugaVeiculos.DTOs.Payments;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDTO(
        Integer paymentId,
        Integer rentId,
        BigDecimal amount,
        String method,
        String status,
        String pixCode,
        LocalDateTime paymentDate
) {
}
