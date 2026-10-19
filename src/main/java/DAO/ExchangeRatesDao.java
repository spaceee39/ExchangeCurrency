package DAO;

import Model.Currency;
import Model.ExchangeRate;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
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
    ;
    """;

    private final String GET_RATE_BY_CODE = """
            SELECT  er.ID       AS rate_id,
                    er.Rate     AS rate,
                    c1.ID       AS base_id,
                    c1.Code     AS base_code,
                    c1.FullName AS base_name,
                    c1.Sign     AS base_sign,
                    c2.ID       AS target_id,
                    c2.Code     AS target_code,
                    c2.FullName AS target_name,
                    c2.Sign     AS target_sign
             FROM exchangerates er
             JOIN currencies c1 ON c1.ID = er.BaseCurrencyId
             JOIN currencies c2 ON c2.ID = er.TargetCurrencyId
             WHERE c1.code = ?
             AND c2.code = ?
              ;
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
            if(exchangeRateList.isEmpty()){
                throw new RuntimeException("No exchange rates found");
            }
            return exchangeRateList;
        } catch (SQLException e) {
            throw new RuntimeException("An unknown error occured");
        }
    }

    public ExchangeRate findByCodes(String baseCurrencyCode, String targetCurrencyCode){
        try (Connection connection = connectionSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_RATE_BY_CODE);){
            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                Currency baseCurrency = new Currency(resultSet.getLong("base_id"), resultSet.getString("base_code"), resultSet.getString("base_name"), resultSet.getString("base_sign"));
                Currency targetCurrency = new Currency(resultSet.getLong("target_id"), resultSet.getString("target_code"), resultSet.getString("target_name"), resultSet.getString("target_sign"));
                return new ExchangeRate(resultSet.getLong("rate_id"), baseCurrency, targetCurrency, resultSet.getBigDecimal("rate"));
            } else throw new RuntimeException("Rate not found");
        } catch (SQLException e) {
            throw new RuntimeException("An unknown error occured");
        }
    }




    public ExchangeRatesDao(HikariDataSource connectionSource) {
        this.connectionSource = connectionSource;
    }
}
