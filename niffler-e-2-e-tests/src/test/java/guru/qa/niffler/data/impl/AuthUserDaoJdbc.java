package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {
  private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  private final Connection connection;

  public AuthUserDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public AuthUserEntity create(AuthUserEntity authUserEntity) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES (?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      preparedStatement.setObject(1, authUserEntity.getUsername());
      preparedStatement.setObject(2, passwordEncoder.encode(authUserEntity.getPassword()));
      preparedStatement.setObject(3, authUserEntity.getEnabled());
      preparedStatement.setObject(4, authUserEntity.getAccountNonExpired());
      preparedStatement.setObject(5, authUserEntity.getAccountNonLocked());
      preparedStatement.setObject(6, authUserEntity.getCredentialsNonExpired());
      preparedStatement.executeUpdate();
      final UUID generatedKey;
      try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
        if (resultSet.next()) {
          generatedKey = resultSet.getObject("id", UUID.class);
        } else {
          throw new SQLException("Не удалось создать запись");
        }
      }
      authUserEntity.setId(generatedKey);
      return authUserEntity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT * FROM \"user\" WHERE id = ?"
    )) {
      preparedStatement.setObject(1, id);
      preparedStatement.execute();
      try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
        if (resultSet.next()) {
          AuthUserEntity authUserEntity = new AuthUserEntity();
          authUserEntity.setId(resultSet.getObject("id", UUID.class));
          authUserEntity.setUsername(resultSet.getString("username"));
          authUserEntity.setPassword(resultSet.getString("password"));
          authUserEntity.setEnabled(resultSet.getBoolean("enabled"));
          authUserEntity.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
          authUserEntity.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
          authUserEntity.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));
          return Optional.of(authUserEntity);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(AuthUserEntity authUserEntity) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "DELETE FROM \"user\" WHERE id = ?"
    )) {
      preparedStatement.setObject(1, authUserEntity.getId());
      int numberEntitiesRemoved = preparedStatement.executeUpdate();
      if (numberEntitiesRemoved != 1) throw new SQLException("Не удалось удалить трату с id = %s из таблицы user".formatted(authUserEntity.getId()));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
