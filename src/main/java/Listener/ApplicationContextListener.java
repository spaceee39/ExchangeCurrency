package Listener;

import Config.DataHikariSourceFabric;
import DAO.CurrenciesDao;
import DAO.ExchangeRatesDao;
import Service.CurrencyService;
import Service.ExchangeRatesService;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import tools.jackson.databind.ObjectMapper;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {

            HikariDataSource connectionSource = DataHikariSourceFabric.create();

            // создаем все, что будем держать в контексте приложения
            CurrenciesDao currenciesDao = new CurrenciesDao(connectionSource);
            ExchangeRatesDao exchangeRatesDao = new ExchangeRatesDao(connectionSource);
            ObjectMapper objectMapper = new ObjectMapper();
            CurrencyService currencyService = new CurrencyService(currenciesDao);
            ExchangeRatesService exchangeRatesService = new ExchangeRatesService(exchangeRatesDao);

            ServletContext servletContext = sce.getServletContext();

            servletContext.setAttribute("currenciesDao", currenciesDao);
            servletContext.setAttribute("exchangeRatesDao", exchangeRatesDao);
            servletContext.setAttribute("objectMapper", objectMapper);
            servletContext.setAttribute("currencyService", currencyService);
            servletContext.setAttribute("exchangeRatesService", exchangeRatesService);
            // кладем пулл в контекст чтобы потом корректно завершить программу
            servletContext.setAttribute("connectionSource", connectionSource);
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        HikariDataSource connectionSource = (HikariDataSource) servletContext.getAttribute("connectionSource");
        connectionSource.close();
    }
}
