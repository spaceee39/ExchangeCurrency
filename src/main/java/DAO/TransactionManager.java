package DAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class TransactionManager {

    public <T> T executeInTransaction(DataSource dataSource, Function<Connection, T> action){
        try (Connection connection = dataSource.getConnection()){
            // отключение автокоммита
            connection.setAutoCommit(false);
            try{
                T result = action.apply(connection);
                connection.commit();
                return result;
            }catch (Exception e){
                connection.rollback();
                throw new RuntimeException("Transaction is rollback.");
            }
        } catch (SQLException e){
            throw new RuntimeException();
        }
    }
}
