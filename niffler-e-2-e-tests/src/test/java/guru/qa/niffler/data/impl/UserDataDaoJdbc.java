package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.UserDataDao;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserDataDaoJdbc implements UserDataDao {
  private final Connection connection;

  public UserDataDaoJdbc(Connection connection) {
    this.connection = connection;
  }
  @Override
  public UserEntity create(UserEntity userEntity) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "INSERT INTO user (username, currency, firstname, surname, fullname, photo, photoSmall) VALUES (?, ?, ?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      )) {
        preparedStatement.setString(1, userEntity.getUsername());
        preparedStatement.setString(2, userEntity.getCurrency().name());
        preparedStatement.setString(3, userEntity.getFirstname());
        preparedStatement.setString(4, userEntity.getSurname());
        preparedStatement.setString(5, userEntity.getFullname());
        preparedStatement.setBytes(6, userEntity.getPhoto());
        preparedStatement.setBytes(7, userEntity.getPhotoSmall());
        preparedStatement.executeUpdate();
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
          UUID generatedKey = null;
          if (resultSet.next()) {
            generatedKey = resultSet.getObject("id", UUID.class);
          }
          userEntity.setId(generatedKey);
          return userEntity;
        }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "SELECT * FROM user WHERE id = ?"
      )) {
        preparedStatement.setObject(1, id);
        preparedStatement.execute();
        try (ResultSet resultSet = preparedStatement.getResultSet()) {
          if (resultSet.next()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(resultSet.getObject("id", UUID.class));
            userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
            userEntity.setFirstname(resultSet.getString("firstname"));
            userEntity.setSurname(resultSet.getString("surname"));
            userEntity.setFullname(resultSet.getString("fullname"));
            userEntity.setPhoto(resultSet.getBytes("photo"));
            userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
            return Optional.of(userEntity);
          } else {
            return Optional.empty();
          }
        }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "SELECT * FROM user WHERE username = ? LIMIT 1"
      )) {
        preparedStatement.setObject(1, username);
        preparedStatement.execute();
        try (ResultSet resultSet = preparedStatement.getResultSet()) {
          if (resultSet.next()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(resultSet.getObject("id", UUID.class));
            userEntity.setCurrency(resultSet.getObject("currency", CurrencyValues.class));
            userEntity.setFirstname(resultSet.getString("firstname"));
            userEntity.setSurname(resultSet.getString("surname"));
            userEntity.setFullname(resultSet.getString("fullname"));
            userEntity.setPhoto(resultSet.getBytes("photo"));
            userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
            return Optional.of(userEntity);
          } else {
            return Optional.empty();
          }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(UserEntity user) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "DELETE FROM user WHERE id = ?"
      )) {
        preparedStatement.setObject(1, user.getId());
        int numberEntitiesRemoved = preparedStatement.executeUpdate();
        if (numberEntitiesRemoved != 1) {
          throw new SQLException("Не удалось удалить пользователя с id = %s из таблицы user".formatted(user.getId()));
        }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}