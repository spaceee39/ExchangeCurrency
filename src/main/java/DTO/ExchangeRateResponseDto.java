package DTO;

import Model.Currency;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonPropertyOrder({"id","baseCurrency","targetCurrency","rate"})
public record ExchangeRateResponseDto(
        Long id,
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal rate
) {
}
