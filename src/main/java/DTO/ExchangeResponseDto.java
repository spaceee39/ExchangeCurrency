package DTO;

import Model.Currency;
import Model.ExchangeRate;

import java.math.BigDecimal;

public record ExchangeResponseDto(
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal amount,
        BigDecimal convertedAmount
) {
}
