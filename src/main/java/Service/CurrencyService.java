package Service;

import DAO.CurrenciesDao;
import DTO.CurrencyDto;
import Exceptions.CurrencyNotFoundException;
import Exceptions.CurrencySaveException;
import Model.Currency;
import java.util.List;


public class CurrencyService {

    private final CurrenciesDao currenciesDao;

    public List<Currency> getCurrencyList(){
        try {
            return currenciesDao.findAll();
        } catch (RuntimeException e) {
            throw new CurrencyNotFoundException("An error occurred while searching for currencies.");
        }
    }

    public Currency getCurrencyByCode(String code) {
        return currenciesDao.findCurrencyByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException("Currency not found: " + code));
    }

    public void addNewCurrency(CurrencyDto currencyDto){
        Currency currency = new Currency(currencyDto);
        currenciesDao.save(currency);
    }

    //constructor
    public CurrencyService(CurrenciesDao currenciesDao){
        this.currenciesDao = currenciesDao;
    }
}
