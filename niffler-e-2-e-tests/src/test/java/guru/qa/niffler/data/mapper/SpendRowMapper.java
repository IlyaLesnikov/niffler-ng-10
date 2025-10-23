package guru.qa.niffler.data.mapper;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendRowMapper implements RowMapper<SpendEntity> {
  private SpendRowMapper() {}

  public static final SpendRowMapper INSTANCE = new SpendRowMapper();
  private final Config CONFIG = Config.getInstance();

  @Override
  public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    SpendEntity spendEntity = new SpendEntity();
    spendEntity.setId(rs.getObject("id", UUID.class));
    spendEntity.setUsername(rs.getString("username"));
    spendEntity.setCurrency(
        CurrencyValues.valueOf(rs.getString("currency"))
    );
    spendEntity.setSpendDate(rs.getDate("spend_date"));
    spendEntity.setAmount(rs.getDouble("amount"));
    spendEntity.setDescription(rs.getString("description"));
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setId(rs.getObject("category_id", UUID.class));
    spendEntity.setCategory(categoryEntity);
    return spendEntity;
  }
}
