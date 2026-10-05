package Listener;

import Config.DataHikariSourceFabric;
import DAO.CurrenciesDao;
import Service.CurrencyService;
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

        HikariDataSource connectionSource = DataHikariSourceFabric.create();

        // создаем все, что будем держать в контексте приложения
        CurrenciesDao currenciesDao = new CurrenciesDao(connectionSource);
        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyService currencyService = new CurrencyService(currenciesDao);

        ServletContext servletContext = sce.getServletContext();

        servletContext.setAttribute("currenciesDao", currenciesDao);
        servletContext.setAttribute("objectMapper", objectMapper);
        servletContext.setAttribute("currencyService", currencyService);
        // кладем пулл в контекст чтобы потом корректно завершить программу
        servletContext.setAttribute("connectionSource", connectionSource);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        HikariDataSource connectionSource = (HikariDataSource) servletContext.getAttribute("connectionSource");
        connectionSource.close();
    }
}
