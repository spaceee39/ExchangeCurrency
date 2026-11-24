package DTO;

import Model.Currency;
import Model.ExchangeRate;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonPropertyOrder({"baseCurrency", "targetCurrency", "rate", "amount", "convertedAmount"})
public record ExchangeResponseDto(
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal amount,
        BigDecimal convertedAmount
) {
}
