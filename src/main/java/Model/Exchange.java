package Model;


import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Exchange {

    private final ExchangeRate exchangeRate;
    private final BigDecimal amount;
    private final BigDecimal convertedAmount;

    public Exchange(ExchangeRate exchangeRate, BigDecimal amount, BigDecimal convertedAmount) {
        this.exchangeRate = exchangeRate;
        this.amount = amount;
        BigDecimal rate = exchangeRate.getRate();
        this.convertedAmount = convertedAmount;
    }
}
