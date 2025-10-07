package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
  private final Connection connection;

  public AuthAuthorityDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public AuthorityEntity create(AuthorityEntity authorityEntity) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "INSERT INTO authority (user_id, authority) VALUES (?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      preparedStatement.setObject(1, authorityEntity.getAuthority());
      preparedStatement.setObject(2, authorityEntity.getUser().getId());
      preparedStatement.executeUpdate();
      final UUID generatedKey;
      try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
        if (resultSet.next()) {
          generatedKey = resultSet.getObject("id", UUID.class);
        } else {
          throw new SQLException("Не удалось создать запись");
        }
      }
      authorityEntity.setId(generatedKey);
      return authorityEntity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthorityEntity> findById(UUID id) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT * FROM authority WHERE id = ?"
    )) {
      preparedStatement.setObject(1, id);
      preparedStatement.execute();
      try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
        if (resultSet.next()) {
          AuthorityEntity authorityEntity = new AuthorityEntity();
          authorityEntity.setId(resultSet.getObject("id", UUID.class));
          authorityEntity.setAuthority(resultSet.getObject("authority", Authority.class));
          authorityEntity.setId(resultSet.getObject("user_id", UUID.class));
          return Optional.of(authorityEntity);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(AuthorityEntity authorityEntity) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "DELETE FROM authority WHERE id = ?"
    )) {
      preparedStatement.setObject(1, authorityEntity.getId());
      int numberEntitiesRemoved = preparedStatement.executeUpdate();
      if (numberEntitiesRemoved != 1) throw new SQLException("Не удалось удалить трату с id = %s из таблицы authority".formatted(authorityEntity.getId()));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
