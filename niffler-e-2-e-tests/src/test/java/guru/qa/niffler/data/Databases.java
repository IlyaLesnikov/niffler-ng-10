package guru.qa.niffler.data;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Databases {
  private Databases() {}

  private static final Map<String, DataSource> datasource = new ConcurrentHashMap<>();

  public static Connection connection(String jdbcUrl) throws SQLException {
    return datasource(jdbcUrl).getConnection();
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
