package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.mapper.SpendRowMapper;
import guru.qa.niffler.data.mapper.SpendsRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SpendDaoSpringJdbc implements SpendDao {
  private final DataSource dataSource;

  public SpendDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public SpendEntity create(SpendEntity spendEntity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
          PreparedStatement preparedStatement = connection.prepareStatement(
              "INSERT INTO spend (username, currency, spend_date, amount, description, category_id) VALUES (?, ?, ?, ?, ?, ?)",
              Statement.RETURN_GENERATED_KEYS
          );
          preparedStatement.setString(1, spendEntity.getUsername());
          preparedStatement.setString(2, spendEntity.getCurrency().name());
          preparedStatement.setDate(3, new Date(spendEntity.getSpendDate().getTime()));
          preparedStatement.setDouble(4, spendEntity.getAmount());
          preparedStatement.setString(5, spendEntity.getDescription());
          preparedStatement.setObject(6, spendEntity.getCategory().getId());
          return preparedStatement;
        },
        keyHolder
    );
    final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
    log.info("The %s entity was created in the spend table".formatted(generatedKey));
    spendEntity.setId(
        generatedKey
    );
    return spendEntity;
  }

  @Override
  public Optional<SpendEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    SpendEntity spendEntity = jdbcTemplate.queryForObject(
        "SELECT * FROM spend WHERE id = ?",
        SpendRowMapper.INSTANCE,
        id
    );
    return Optional.ofNullable(spendEntity);
  }

  @Override
  public List<SpendEntity> findAllByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.queryForObject(
        "SELECT * FROM spend WHERE username = ?",
        SpendsRowMapper.INSTANCE
    );
  }

  @Override
  public void delete(SpendEntity spendEntity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
        "DELETE FROM spend WHERE id = ?",
        spendEntity.getId()
    );
    log.info("The entity with %s was removed from the Spend table".formatted(spendEntity.getId()));
  }
}
