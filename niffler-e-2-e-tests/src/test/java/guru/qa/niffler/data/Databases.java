package guru.qa.niffler.data;

import guru.qa.niffler.model.TransactionIsolation;
import guru.qa.niffler.model.XaFunction;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Databases {
  private Databases() {}

  private static final Map<String, DataSource> datasource = new ConcurrentHashMap<>();
  private static final Map<Long, Map<String, Connection>> threadConnections = new ConcurrentHashMap<>();

  /*public static <T> T xaTransaction(XaFunction<T>... actions) {
    UserTransaction userTransaction;
    try {
      userTransaction.begin();
      T result;
      for (XaFunction<T> action: actions) {
        Connection connection = connection(action.jdbcUrl());
        result = action.function(connection);
      }
      userTransaction.commit();
      return result;
    } catch (SystemException e) {
      try {
        userTransaction.rollback();
      } catch (SystemException ex) {
        throw new RuntimeException(ex);
      }
      throw new RuntimeException(e);
    }
  }*/

  public static void xaTransaction() {

  }

  public static <T> T transaction(Function<Connection, T> function, String jdbcUrl, TransactionIsolation transactionIsolation) {
    Connection connection = null;
    try {
      connection = connection(jdbcUrl);
      connection.setAutoCommit(false);
      connection.setTransactionIsolation(transactionIsolation.level());
      T result = function.apply(connection);
      connection.commit();
      connection.setAutoCommit(true);
      return result;
    } catch (SQLException e) {
      try {
        if (connection != null) {
          connection.rollback();
          connection.setAutoCommit(true);
        }
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
      throw new RuntimeException(e);
    }
  }

  public static void transaction(Consumer<Connection> consumer, String jdbcUrl, TransactionIsolation transactionIsolation) {
    Connection connection = null;
    try {
      connection = connection(jdbcUrl);
      connection.setAutoCommit(false);
      connection.setTransactionIsolation(transactionIsolation.level());
      consumer.accept(connection);
      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      try {
        if (connection != null) {
          connection.rollback();
          connection.setAutoCommit(true);
        }
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
      throw new RuntimeException(e);
    }
  }

  public static Connection connection(String jdbcUrl) throws SQLException {
    return threadConnections.computeIfAbsent(
        Thread.currentThread().threadId(),
        key -> {
          try {
            return new HashMap<>(
                Map.of(jdbcUrl, datasource(jdbcUrl).getConnection())
            );
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
    ).computeIfAbsent(
        jdbcUrl,
        key -> {
          try {
            return datasource(jdbcUrl).getConnection();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
    );
  }

  private static DataSource datasource(String jdbcUrl) {
    return datasource.computeIfAbsent(
        jdbcUrl,
        key -> {
          PGSimpleDataSource dataSource = new PGSimpleDataSource();
          dataSource.setUrl(jdbcUrl);
          dataSource.setUser("postgres");
          dataSource.setPassword("secret");
          return dataSource;
        }
    );
  }
}
