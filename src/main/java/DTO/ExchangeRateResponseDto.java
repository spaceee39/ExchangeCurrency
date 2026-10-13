package DTO;

import Model.Currency;

import java.math.BigDecimal;

public record ExchangeRateResponseDto(
        Long id,
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal rate
) {
}
