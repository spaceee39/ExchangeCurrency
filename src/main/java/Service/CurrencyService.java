package Service;

import DAO.CurrenciesDao;
import DTO.CurrencyRequestDto;
import DTO.CurrencyResponseDto;
import Exceptions.CurrencyNotFoundException;
import Exceptions.ExchangeRateNotFoundException;
import Model.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CurrencyService {

    private final CurrenciesDao currenciesDao;

    public List<CurrencyResponseDto> getCurrencyList(){
        List<Currency> currencyList = currenciesDao.findAll();
        List<CurrencyResponseDto> currencyDtoList = new ArrayList<>();
        for(Currency currency : currencyList){
            currencyDtoList.add(new CurrencyResponseDto(currency.getId(),currency.getName(), currency.getCode(), currency.getSign()));
        }
        return currencyDtoList;
    }

    public CurrencyResponseDto getCurrencyByCode(String code) {
        Currency currency = currenciesDao.findCurrencyByCode(code).orElseThrow(() -> new CurrencyNotFoundException("Currency not found: " + code));
        return new CurrencyResponseDto(currency.getId(), currency.getName(), currency.getCode(), currency.getSign());
    }

    public CurrencyResponseDto addNewCurrency(CurrencyRequestDto currencyRequestDto){
        Currency currency = new Currency(currencyRequestDto);
        currenciesDao.save(currency);
        Optional<Currency> cur = currenciesDao.findCurrencyByCode(currency.getCode());
        if(cur.isPresent()) {
            return new CurrencyResponseDto(cur.get().getId(), cur.get().getName(), cur.get().getCode(), cur.get().getSign());
        } else throw new CurrencyNotFoundException("Currency not found");
    }

    //constructor
    public CurrencyService(CurrenciesDao currenciesDao){
        this.currenciesDao = currenciesDao;
    }
}
