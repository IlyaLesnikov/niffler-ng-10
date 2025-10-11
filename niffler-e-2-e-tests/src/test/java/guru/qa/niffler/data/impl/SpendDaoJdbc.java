package guru.qa.niffler.data.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {
  private final static Config CONFIG = Config.getInstance();

  @Override
  public SpendEntity create(SpendEntity spendEntity) {
    try (Connection connection = Databases.connection(CONFIG.spendJdbcUrl())) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "INSERT INTO spend (username, currency, spend_date, amount, description, category_id) VALUES (?, ?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      )) {
        preparedStatement.setString(1, spendEntity.getUsername());
        preparedStatement.setString(2, spendEntity.getCurrency().name());
        preparedStatement.setDate(3, new Date(spendEntity.getSpendDate().getTime()));
        preparedStatement.setDouble(4, spendEntity.getAmount());
        preparedStatement.setString(5, spendEntity.getDescription());
        preparedStatement.setObject(6, spendEntity.getCategory().getId());
        preparedStatement.executeUpdate();
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
          if (resultSet.next()) {
            UUID generatedKey = resultSet.getObject("id", UUID.class);
            spendEntity.setId(generatedKey);
          }
          return spendEntity;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<SpendEntity> findById(UUID id) {
    try (Connection connection = Databases.connection(CONFIG.spendJdbcUrl())) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "SELECT * FROM spend WHERE id = ?"
      )) {
        preparedStatement.setObject(1, id);
        preparedStatement.execute();
        try (ResultSet resultSet = preparedStatement.getResultSet()) {
          if (resultSet.next()) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(resultSet.getObject("category_id", UUID.class));
            SpendEntity spendEntity = new SpendEntity();
            spendEntity.setUsername(resultSet.getString("username"));
            spendEntity.setCurrency(
                CurrencyValues.valueOf(resultSet.getString("currency"))
            );
            spendEntity.setSpendDate(resultSet.getDate("spendDate"));
            spendEntity.setAmount(resultSet.getDouble("amount"));
            spendEntity.setDescription(resultSet.getString("username"));
            spendEntity.setCategory(categoryEntity);
            return Optional.of(spendEntity);
          } else {
            return Optional.empty();
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<SpendEntity> findAllByUsername(String username) {
    try (Connection connection = Databases.connection(CONFIG.spendJdbcUrl())) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "SELECT * FROM spend WHERE username = ?"
      )) {
        preparedStatement.setObject(1, username);
        preparedStatement.execute();
        try (ResultSet resultSet = preparedStatement.getResultSet()) {
          List<SpendEntity> spendEntities = new ArrayList<>();
          while (resultSet.next()) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(resultSet.getObject("category_id", UUID.class));
            SpendEntity spendEntity = new SpendEntity();
            spendEntity.setUsername(resultSet.getString("username"));
            spendEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("username")));
            spendEntity.setSpendDate(resultSet.getDate("spend_date"));
            spendEntity.setAmount(resultSet.getDouble("amount"));
            spendEntity.setDescription(resultSet.getString("username"));
            spendEntity.setCategory(categoryEntity);
            spendEntities.add(spendEntity);
          }
          return spendEntities;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(SpendEntity spendEntity) {
    try (Connection connection = Databases.connection(CONFIG.spendJdbcUrl())) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "DELETE FROM spend WHERE id = ?"
      )) {
        preparedStatement.setObject(1, spendEntity.getId());
        int numberEntitiesRemoved = preparedStatement.executeUpdate();
        if (numberEntitiesRemoved != 1) {
          throw new SQLException("Не удалось удалить трату с id = %s из таблицы spend".formatted(spendEntity.getId()));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
