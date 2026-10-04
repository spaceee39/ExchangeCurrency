package Service;

import DAO.CurrenciesDao;
import DAO.CurrencyDao;
import Exceptions.CurrenciesNotFoundException;
import Model.Currency;
import com.zaxxer.hikari.HikariDataSource;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static Config.DataHikariSourceFabric.create;

public class CurrencyService {

    // пулл наших connection
    HikariDataSource hikariDataSource = create();
    // создаем дао и прокидываем туда ссылку на пул
    CurrenciesDao currenciesDAO = new CurrenciesDao(hikariDataSource);
    CurrencyDao currencyDao = new CurrencyDao(hikariDataSource);

    ObjectMapper mapper = new ObjectMapper();

    public String getCurrencyListJson(){
        try {
            List<Currency> currencyList = currenciesDAO.findAll();
            String json = mapper.writeValueAsString(currencyList);
            return json;
        } catch (RuntimeException e) {
            throw new CurrenciesNotFoundException("An error occurred while searching for currencies.");
        }

    }

    public String getCurrencyJson(String code){
        Currency currency = currencyDao.findCurrencyByCode(code);
    }



}
