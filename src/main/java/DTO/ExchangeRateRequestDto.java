package DTO;

import java.math.BigDecimal;

public record ExchangeRateRequestDto(
        String BaseCurrencyCode,
        String TargetCurrencyCode,
        BigDecimal rate
) {
}
