package Config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Properties;

import static Config.ConfigLoader.loadConfig;

public class DataHikariSourceFabric {

    //статик который возвращает наше хранилище данных (Connection), наш так называемый Пул
    public static HikariDataSource create(){
        Properties dbConfig = loadConfig();

        // создаем конфиг нашего пула соединений
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbConfig.getProperty("jdbc.url"));
        hikariConfig.setUsername(dbConfig.getProperty("jdbc.username"));
        hikariConfig.setPassword(dbConfig.getProperty("jdbc.password"));
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return new HikariDataSource(hikariConfig);
        }


}
