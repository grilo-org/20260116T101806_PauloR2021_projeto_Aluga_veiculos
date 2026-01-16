package com.pauloricardo.alugaVeiculos.Exceptions;

import java.time.LocalDateTime;

public record ApiErrorDTO(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
}
