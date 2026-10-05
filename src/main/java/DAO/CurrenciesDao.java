package DAO;

import Model.Currency;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDao {

    private final HikariDataSource connectionSource;

    //константы(запросы в баз)
    private static final String GET_ALL_CURRENCIES = "SELECT * FROM currencies;";
    private static final String GET_BY_CODE = "SELECT * FROM currencies WHERE code = ?;";


    public List<Currency> findAll(){
        List<Currency> currencyList = new ArrayList<>();
        try(
            Connection connection = connectionSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_CURRENCIES);
            ) {
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

    public Currency findCurrencyByCode(String code){
        try(
            Connection connection = connectionSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_BY_CODE);
        ) {

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public CurrenciesDao(HikariDataSource connectionSource) {
        this.connectionSource = connectionSource;
    }
}
