package DAO;

import Model.Currency;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CurrencyDao {

    HikariDataSource connectionSource;

    public CurrencyDao(HikariDataSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public Currency findCurrencyByCode(String code){
        String sqlRequest = "select * from currencies where code = ?";
        try(
            Connection connection = connectionSource.getConnection();
            Statement statement = connection.createStatement();
            
            ResultSet resultSet = statement.executeQuery(sqlRequest);
        ) {

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
