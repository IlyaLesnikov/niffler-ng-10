package guru.qa.niffler.data;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import guru.qa.niffler.model.TransactionIsolation;
import guru.qa.niffler.model.XaConsumer;
import guru.qa.niffler.model.XaFunction;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.substringAfter;

public class Databases {
  private Databases() {}

  private static final Map<String, DataSource> datasource = new ConcurrentHashMap<>();
  private static final Map<Long, Map<String, Connection>> threadConnections = new ConcurrentHashMap<>();

  public static <T> T xaTransaction(TransactionIsolation transactionIsolation, XaFunction<T>... actions) {
    UserTransaction userTransaction = new UserTransactionImp();
    try {
      userTransaction.begin();
      T result = null;
      for (XaFunction<T> action: actions) {
        Connection connection = connection(action.jdbcUrl());
        connection.setTransactionIsolation(transactionIsolation.level());
        result = action.function().apply(connection);
      }
      userTransaction.commit();
      return result;
    } catch (Exception e) {
      try {
        userTransaction.rollback();
      } catch (SystemException ex) {
        throw new RuntimeException(ex);
      }
      throw new RuntimeException(e);
    }
  }

  public static void xaTransaction(TransactionIsolation transactionIsolation, XaConsumer... actions) {
    UserTransaction userTransaction = new UserTransactionImp();
    try {
      userTransaction.begin();
      for (XaConsumer action: actions) {
        Connection connection = connection(action.jdbcUrl());
        connection.setTransactionIsolation(transactionIsolation.level());
        action.consumer().accept(connection);
      }
      userTransaction.commit();
    } catch (Exception e) {
      try {
        userTransaction.rollback();
      } catch (SystemException ex) {
        throw new RuntimeException(ex);
      }
      throw new RuntimeException(e);
    }
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
          AtomikosDataSourceBean xaDatasource = new AtomikosDataSourceBean();
          final String uniqueId = substringAfter(jdbcUrl, "5432/");
          xaDatasource.setUniqueResourceName(uniqueId);
          xaDatasource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
          Properties properties = new Properties();
          properties.setProperty("URL", jdbcUrl);
          properties.setProperty("user", "postgres");
          properties.setProperty("password", "secret");
          xaDatasource.setXaProperties(properties);
          return xaDatasource;
        }
    );
  }
}
