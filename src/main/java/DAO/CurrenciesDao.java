package DAO;

import Model.Currency;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDao {

    HikariDataSource connectionSource;

    public CurrenciesDao(HikariDataSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public List<Currency> AllCurrencies(){
        String sqlRequest = "select * from currencies";
        List<Currency> currencyList = new ArrayList<>();
        try(
            Connection connection = connectionSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlRequest);
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



}
