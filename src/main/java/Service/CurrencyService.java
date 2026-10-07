package Service;

import DAO.CurrenciesDao;
import Exceptions.CurrencyNotFoundException;
import Model.Currency;
import java.util.List;


public class CurrencyService {

    private final CurrenciesDao currenciesDAO;

    public List<Currency> getCurrencyList(){
        try {
            return currenciesDAO.findAll();
        } catch (RuntimeException e) {
            throw new CurrencyNotFoundException("An error occurred while searching for currencies.");
        }
    }

    public Currency getCurrencyByCode(String code) {
        return currenciesDAO.findCurrencyByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException("Currency not found: " + code));
    }

    public CurrencyService(CurrenciesDao currenciesDao){
        this.currenciesDAO = currenciesDao;
    }
}
