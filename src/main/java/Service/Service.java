package Service;

import DAO.CurrenciesDAO;
import Model.Currency;
import com.zaxxer.hikari.HikariDataSource;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static Config.DataHikariSourceFabric.create;

public class Service {

    // пулл наших connection
    HikariDataSource hikariDataSource = create();
    // создаем дао и прокидываем туда ссылку на пул
    CurrenciesDAO currenciesDAO = new CurrenciesDAO(hikariDataSource);

    ObjectMapper mapper = new ObjectMapper();

    public String currencyListJsonResp(){
        List<Currency> currencyList = currenciesDAO.AllCurrencies();
        String json = mapper.writeValueAsString(currencyList);
        return json;
    }


}
