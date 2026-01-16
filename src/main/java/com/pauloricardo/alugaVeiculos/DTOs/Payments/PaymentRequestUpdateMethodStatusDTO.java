package com.pauloricardo.alugaVeiculos.DTOs.Payments;

public record PaymentRequestUpdateMethodStatusDTO(
        EnumPaymentMethod method,
        EnumPaymentStatus status
) {
}
