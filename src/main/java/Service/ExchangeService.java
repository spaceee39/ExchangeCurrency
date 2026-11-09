package Service;

import DAO.ExchangeRatesDao;
import DTO.ExchangeRequestDto;
import DTO.ExchangeResponseDto;
import Exceptions.ExchangeRateNotFoundException;
import Model.Exchange;
import Model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class ExchangeService {

    ExchangeRatesDao exchangeRatesDao;

    public ExchangeResponseDto makeExchange(ExchangeRequestDto exchangeRequestDto) {
        List<ExchangeRate> rates = exchangeRatesDao.findAll();
        Optional<ExchangeRate> exchangeRate = findRateForExchange(exchangeRequestDto.baseCurrencyCode(), exchangeRequestDto.targetCurrencyCode(), rates);
        if(exchangeRate.isPresent()){
            return new ExchangeResponseDto(exchangeRate.get(), exchangeRequestDto.amount(), getConvertedAmount(exchangeRate.get(), exchangeRequestDto.amount()));
        } else {
            Optional<ExchangeRate> exToUsd = findRateForExchange(exchangeRequestDto.baseCurrencyCode(), "USD", rates);
            Optional<ExchangeRate> exFromUsd = findRateForExchange(exchangeRequestDto.targetCurrencyCode(), "USD", rates);
            if(exToUsd.isEmpty() | exFromUsd.isEmpty()) throw new ExchangeRateNotFoundException("Exchange rate not found");
            BigDecimal rateThroughUsd = exFromUsd.get().getRate().multiply(exFromUsd.get().getRate());
            ExchangeRate ex = new ExchangeRate(exToUsd.get().getBaseCurrencyId(), exFromUsd.get().getTargetCurrencyId(), rateThroughUsd);
            return new ExchangeResponseDto(ex, exchangeRequestDto.amount(), getConvertedAmount(ex, exchangeRequestDto.amount()));
        }
    }

    public ExchangeService(ExchangeRatesDao exchangeRatesDao) {
        this.exchangeRatesDao = exchangeRatesDao;
    }

    private Optional<ExchangeRate> findRateForExchange(String baseCode, String targetCode, List<ExchangeRate> rates){
        for (ExchangeRate exRate : rates){
            //прямой обмен
            if(exRate.getBaseCurrencyId().getCode().equals(baseCode)&&
               exRate.getTargetCurrencyId().getCode().equals(targetCode)){
                return Optional.of(exRate);
            }
            //через обратный курс
            if (exRate.getBaseCurrencyId().getCode().equals(targetCode) &&
                    exRate.getTargetCurrencyId().getCode().equals(baseCode)) {
                BigDecimal inverseRate = BigDecimal.ONE.divide(exRate.getRate(), 6, RoundingMode.HALF_UP);
                return Optional.of(new ExchangeRate(exRate.getTargetCurrencyId(), exRate.getBaseCurrencyId(), inverseRate));
            }
        }
        return Optional.empty();
    }

    private BigDecimal getConvertedAmount(ExchangeRate exchangeRate, BigDecimal amount){
        return amount.multiply(exchangeRate.getRate());
    }
}
