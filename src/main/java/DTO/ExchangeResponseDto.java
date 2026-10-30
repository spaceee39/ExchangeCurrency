package DTO;

import Model.ExchangeRate;

import java.math.BigDecimal;

public record ExchangeResponseDto(
        ExchangeRate exchangeRate,
        BigDecimal amount,
        BigDecimal convertedAmount
) {
}
