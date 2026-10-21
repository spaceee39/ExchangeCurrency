package Model;

import DTO.ExchangeRateRequestDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@JsonPropertyOrder("id, baseCurrencyId;, targetCurrencyId, rate")
public class ExchangeRate {
    private Long id;
    private final Currency baseCurrencyId;
    private final Currency targetCurrencyId;
    private final BigDecimal rate;

    public ExchangeRate(Long id, Currency baseCurrencyId, Currency targetCurrencyId, BigDecimal rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public ExchangeRate(ExchangeRateRequestDto exchangeRateRequestDto) {
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }
}
