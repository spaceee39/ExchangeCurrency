package Service;

import DAO.CurrenciesDao;
import DTO.CurrencyRequestDto;
import DTO.CurrencyResponseDto;
import Exceptions.CurrencyNotFoundException;
import Model.Currency;

import java.util.ArrayList;
import java.util.List;


public class CurrencyService {

    private final CurrenciesDao currenciesDao;

    public List<CurrencyResponseDto> getCurrencyList(){
        List<Currency> currencyList = currenciesDao.findAll();
        List<CurrencyResponseDto> currencyDtoList = new ArrayList<>();
        for(Currency currency : currencyList){
            currencyDtoList.add(new CurrencyResponseDto(currency.getId(),currency.getFullName(), currency.getCode(), currency.getSign()));
        }
        return currencyDtoList;
    }

    public CurrencyResponseDto getCurrencyByCode(String code) {
        Currency currency = currenciesDao.findCurrencyByCode(code).orElseThrow(() -> new CurrencyNotFoundException("Currency not found: " + code));
        return new CurrencyResponseDto(currency.getId(), currency.getFullName(), currency.getCode(), currency.getSign());
    }

    public void addNewCurrency(CurrencyRequestDto currencyRequestDto){
        Currency currency = new Currency(currencyRequestDto);
        currenciesDao.save(currency);
    }

    //constructor
    public CurrencyService(CurrenciesDao currenciesDao){
        this.currenciesDao = currenciesDao;
    }
}
