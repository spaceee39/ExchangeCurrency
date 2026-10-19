package Service;

import DAO.ExchangeRatesDao;
import DTO.ExchangeRateResponseDto;
import Model.ExchangeRate;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesService {
    private final ExchangeRatesDao exchangeRatesDao;

    public List<ExchangeRateResponseDto> getAllExchangeRates(){
        List<ExchangeRate> exchangeRateList = exchangeRatesDao.findAll();
        List<ExchangeRateResponseDto> responseDtos = new ArrayList<>();
        for(ExchangeRate exchangeRate : exchangeRateList){
            responseDtos.add(new ExchangeRateResponseDto(exchangeRate.getId(), exchangeRate.getBaseCurrencyId(), exchangeRate.getTargetCurrencyId(), exchangeRate.getRate()));
        }
        return responseDtos;
    }

    public ExchangeRateResponseDto getRateByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        ExchangeRate exchangeRate = exchangeRatesDao.findByCodes(baseCurrencyCode, targetCurrencyCode);
        return new ExchangeRateResponseDto(exchangeRate.getId(), exchangeRate.getBaseCurrencyId(), exchangeRate.getTargetCurrencyId(), exchangeRate.getRate());
    }

    public ExchangeRatesService(ExchangeRatesDao exchangeRatesDao) {
        this.exchangeRatesDao = exchangeRatesDao;
    }
}
