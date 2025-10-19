package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.mapper.CategoriesRowMapper;
import guru.qa.niffler.data.mapper.CategoryRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class CategoryDaoSpringJdbc implements CategoryDao {
  private final DataSource dataSource;

  public CategoryDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public CategoryEntity create(CategoryEntity categoryEntity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement preparedStatement = connection.prepareStatement(
          "INSERT INTO category (name, username, archived) VALUES (?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      preparedStatement.setString(1, categoryEntity.getName());
      preparedStatement.setString(2, categoryEntity.getUsername());
      preparedStatement.setBoolean(3, categoryEntity.isArchived());
      return preparedStatement;
    }, keyHolder);
    final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
    log.info("An entity with an %s has been created in the category table".formatted(generatedKey));
    categoryEntity.setId(
        generatedKey
    );
    return categoryEntity;
  }

  @Override
  public Optional<CategoryEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    CategoryEntity categoryEntity = jdbcTemplate.queryForObject(
        "SELECT * FROM category WHERE id = ?",
        CategoryRowMapper.INSTANCE,
        id
    );
    return Optional.ofNullable(categoryEntity);
  }

  @Override
  public Optional<CategoryEntity> findByUsernameAndCategoryName(String username, String categoryName) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    CategoryEntity categoryEntity = jdbcTemplate.queryForObject(
        "SELECT * FROM category WHERE username = ? AND categoryName = ? LIMIT 1",
        CategoryRowMapper.INSTANCE,
        username,
        categoryName
    );
    return Optional.ofNullable(categoryEntity);
  }

  @Override
  public List<CategoryEntity> findAllByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    return jdbcTemplate.queryForObject(
        "SELECT * FROM category WHERE username = ?",
        CategoriesRowMapper.INSTANCE,
        username
    );
  }

  @Override
  public CategoryEntity update(CategoryEntity categoryEntity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update("UPDATE category SET archived = ? WHERE id = ?");
    log.info("Updated the entity with id = %s in the category table".formatted(categoryEntity.getId()));
    return categoryEntity;
  }

  @Override
  public void delete(CategoryEntity categoryEntity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
        "DELETE FROM category WHERE id = ?",
        categoryEntity.getId()
    );
    log.info("The entity with %s has been removed from the category table".formatted(categoryEntity.getId()));
  }
}
