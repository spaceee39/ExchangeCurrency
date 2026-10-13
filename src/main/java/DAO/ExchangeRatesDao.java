package DAO;

import Model.Currency;
import Model.ExchangeRate;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDao {

    private final HikariDataSource connectionSource;

    private static final String GET_ALL_RATES = """
    SELECT er.ID            AS rate_id,
           er.Rate          AS rate,
           c1.ID            AS base_id,
           c1.Code          AS base_code,
           c1.FullName      AS base_name,
           c1.Sign          AS base_sign,
           c2.ID            AS target_id,
           c2.Code          AS target_code,
           c2.FullName      AS target_name,
           c2.Sign          AS target_sign
    FROM exchangerates er
    JOIN currencies c1 ON c1.ID = er.BaseCurrencyId
    JOIN currencies c2 ON c2.ID = er.TargetCurrencyId
    """;

    public List<ExchangeRate> findAll(){
        try (Connection connection = connectionSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_RATES);){
            List<ExchangeRate> exchangeRateList = new ArrayList<>();
            while (resultSet.next()){
                Currency baseCurrency = new Currency(resultSet.getLong("base_id"), resultSet.getString("base_code"), resultSet.getString("base_name"), resultSet.getString("base_sign"));
                Currency targetCurrency = new Currency(resultSet.getLong("target_id"), resultSet.getString("target_code"), resultSet.getString("target_name"), resultSet.getString("target_sign"));
                ExchangeRate exchangeRate = new ExchangeRate(resultSet.getLong("rate_id"), baseCurrency, targetCurrency, resultSet.getBigDecimal("rate"));
                exchangeRateList.add(exchangeRate);
            }
            return exchangeRateList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    public ExchangeRatesDao(HikariDataSource connectionSource) {
        this.connectionSource = connectionSource;
    }
}
