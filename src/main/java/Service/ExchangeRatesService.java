package Service;

import DAO.CurrenciesDao;
import DAO.ExchangeRatesDao;
import DTO.ExchangeRateRequestDto;
import DTO.ExchangeRateResponseDto;
import Exceptions.CurrencyNotFoundException;
import Model.Currency;
import Model.ExchangeRate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesService {
    private final ExchangeRatesDao exchangeRatesDao;
    private final CurrenciesDao currenciesDao;

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

    public void createNewRate(ExchangeRateRequestDto exchangeRateRequestDto){
        Optional<Currency> baseCurrency = currenciesDao.findCurrencyByCode(exchangeRateRequestDto.BaseCurrencyCode());
        Optional<Currency> targetCurrency = currenciesDao.findCurrencyByCode(exchangeRateRequestDto.TargetCurrencyCode());
        if(baseCurrency.isEmpty() || targetCurrency.isEmpty()){
            throw new CurrencyNotFoundException("One (or both) currencies of a currency pair does not exist");
        }
        exchangeRatesDao.save(baseCurrency.get(), targetCurrency.get(), exchangeRateRequestDto.rate());
    }

    public void patchRate(ExchangeRateRequestDto exchangeRateRequestDto){
        exchangeRatesDao.updateRate(exchangeRateRequestDto.BaseCurrencyCode(), exchangeRateRequestDto.TargetCurrencyCode(), exchangeRateRequestDto.rate());
    }

    public ExchangeRatesService(ExchangeRatesDao exchangeRatesDao, CurrenciesDao currenciesDao) {
        this.exchangeRatesDao = exchangeRatesDao;
        this.currenciesDao = currenciesDao;
    }
}
