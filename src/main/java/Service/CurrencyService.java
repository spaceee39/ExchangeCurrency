package Service;

import DAO.CurrenciesDao;
import Exceptions.CurrenciesNotFoundException;
import Model.Currency;
import java.util.List;


public class CurrencyService {

    private final CurrenciesDao currenciesDAO;

    public List<Currency> getCurrencyList(){
        try {
            return currenciesDAO.findAll();
        } catch (RuntimeException e) {
            throw new CurrenciesNotFoundException("An error occurred while searching for currencies.");
        }
    }

    public String getCurrencyByCode(String code){
        Currency currency = currenciesDAO.findCurrencyByCode(code);
    }

    public CurrencyService(CurrenciesDao currenciesDao){
        this.currenciesDAO = currenciesDao;
    }
}
