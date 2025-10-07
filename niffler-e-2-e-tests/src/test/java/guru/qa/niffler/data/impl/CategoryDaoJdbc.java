package guru.qa.niffler.data.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.CategoryEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {
  private final static Config CONFIG = Config.getInstance();

  @Override
  public CategoryEntity create(CategoryEntity categoryEntity) {
    try (Connection connection = Databases.connection(CONFIG.spendJdbcUrl())) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "INSERT INTO category (name, username, archived) VALUES (?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      )) {
        preparedStatement.setString(1, categoryEntity.getName());
        preparedStatement.setString(2, categoryEntity.getUsername());
        preparedStatement.setBoolean(3, categoryEntity.isArchived());
        preparedStatement.executeUpdate();
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
          if (resultSet.next()) {
            UUID generatedKey = resultSet.getObject("id", UUID.class);
            categoryEntity.setId(generatedKey);
          }
          return categoryEntity;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<CategoryEntity> findById(UUID id) {
    try (Connection connection = Databases.connection(CONFIG.spendJdbcUrl())) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "SELECT * FROM category WHERE id = ?"
      )) {
        preparedStatement.setObject(1, id);
        preparedStatement.executeUpdate();
        try (ResultSet resultSet = preparedStatement.getResultSet()) {
          if (resultSet.next()) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(resultSet.getObject("id", UUID.class));
            categoryEntity.setName(resultSet.getString("name"));
            categoryEntity.setUsername(resultSet.getString("username"));
            categoryEntity.setArchived(resultSet.getBoolean("archived"));
            return Optional.of(categoryEntity);
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
  public Optional<CategoryEntity> findByUsernameAndCategoryName(String username, String categoryName) {
    try (Connection connection = Databases.connection(CONFIG.spendJdbcUrl())) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "SELECT * FROM category WHERE username = ? AND categoryName = ?"
      )) {
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, categoryName);
        preparedStatement.executeUpdate();
        try (ResultSet resultSet = preparedStatement.getResultSet()) {
          if (resultSet.next()) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(resultSet.getObject("id", UUID.class));
            categoryEntity.setName(resultSet.getString("name"));
            categoryEntity.setUsername(resultSet.getString("username"));
            categoryEntity.setArchived(resultSet.getBoolean("archived"));
            return Optional.of(categoryEntity);
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
  public List<CategoryEntity> findAllByUsername(String username) {
    try (Connection connection = Databases.connection(CONFIG.spendJdbcUrl())) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "SELECT * FROM category WHERE username = ?"
      )) {
        preparedStatement.setString(1, username);
        preparedStatement.executeUpdate();
        try (ResultSet resultSet = preparedStatement.getResultSet()) {
          List<CategoryEntity> categoryEntities = new ArrayList<>();
          while (resultSet.next()) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(categoryEntity.getId());
            categoryEntity.setName(categoryEntity.getName());
            categoryEntity.setUsername(categoryEntity.getUsername());
            categoryEntity.setArchived(categoryEntity.isArchived());
            categoryEntities.add(categoryEntity);
          }
          return categoryEntities;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CategoryEntity update(CategoryEntity categoryEntity) {
    try (Connection connection = Databases.connection(CONFIG.spendJdbcUrl())) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "UPDATE category SET name = ?, username = ?, archived = ? WHERE id = ?",
          Statement.RETURN_GENERATED_KEYS
      )) {
        preparedStatement.setString(1, categoryEntity.getName());
        preparedStatement.setString(2, categoryEntity.getUsername());
        preparedStatement.setBoolean(3, categoryEntity.isArchived());
        preparedStatement.setObject(4, categoryEntity.getId());
        preparedStatement.executeUpdate();
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
          if (resultSet.next()) {
            return categoryEntity;
          } else {
            throw new SQLException("Не удалось обновить сущность");
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(CategoryEntity categoryEntity) {
    try (Connection connection = Databases.connection(CONFIG.spendJdbcUrl())) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "DELETE FROM category WHERE id = ?"
      )) {
        preparedStatement.setObject(1, categoryEntity.getId());
        int numberEntitiesRemoved = preparedStatement.executeUpdate();
        if (numberEntitiesRemoved != 1) {
          throw new SQLException("Не удалось удалить трату с id = %s из таблицы category".formatted(categoryEntity.getId()));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
