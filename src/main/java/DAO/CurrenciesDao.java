package DAO;

import Exceptions.CurrencyAlreadyExists;
import Exceptions.CurrencySaveException;
import Model.Currency;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesDao {

    private final HikariDataSource connectionSource;

    //константы(запросы в баз)
    private static final String GET_ALL_CURRENCIES = "SELECT * FROM currencies;";
    private static final String GET_BY_CODE = "SELECT * FROM currencies WHERE code = ?;";
    private static final String ADD_NEW_CURRENCY = "INSERT INTO currencies (code, FullName, Sign) value ('?', '?', '?');";


    public List<Currency> findAll(){
        List<Currency> currencyList = new ArrayList<>();
        try(Connection connection = connectionSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_CURRENCIES);) {
            while (resultSet.next()){
                int id = resultSet.getInt("ID");
                String code = resultSet.getString("code");
                String fullName = resultSet.getString("FullName");
                String sign = resultSet.getString("Sign");
                Currency currency = new Currency(id, code, fullName, sign);
                currencyList.add(currency);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencyList;
    }

    public Optional<Currency> findCurrencyByCode(String currencyCode) {
        try (Connection connection = connectionSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_CODE)) {
            statement.setString(1, currencyCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String code = resultSet.getString("code");
                    String fullName = resultSet.getString("FullName");
                    String sign = resultSet.getString("Sign");
                    return Optional.of(new Currency(id, code, fullName, sign));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty(); // валюта не найдена
    }

    public void save(Currency currency){
        if (currency.getCode() == null || currency.getFullName() == null || currency.getSign() == null) {
            throw new IllegalArgumentException("Currency fields must not be null");
        }
        try(Connection connection = connectionSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_CURRENCY);) {
            preparedStatement.setString(1,currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            int affected = preparedStatement.executeUpdate();
            } catch (SQLException e) {
            if(e.getErrorCode() == 1062){
                throw new CurrencyAlreadyExists("This currency already exists", e);
            }
            throw new CurrencySaveException("Currency not saved" + currency.getCode(), e);
        }
    }


    public CurrenciesDao(HikariDataSource connectionSource) {
        this.connectionSource = connectionSource;
    }
}
