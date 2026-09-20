package DAO;

import com.zaxxer.hikari.HikariDataSource;

public class CurrenciesDAO {

    HikariDataSource connectionSource;

    public CurrenciesDAO(HikariDataSource connectionSource) {
        this.connectionSource = connectionSource;
    }

}
