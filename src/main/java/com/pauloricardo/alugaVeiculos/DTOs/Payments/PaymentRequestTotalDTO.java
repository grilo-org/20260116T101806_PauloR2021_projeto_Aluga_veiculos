package com.pauloricardo.alugaVeiculos.DTOs.Payments;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PaymentRequestTotalDTO(
        Integer id_rent,
        BigDecimal amount,
        EnumPaymentMethod method,
        EnumPaymentStatus status,
        String transactionCode
) {
}
