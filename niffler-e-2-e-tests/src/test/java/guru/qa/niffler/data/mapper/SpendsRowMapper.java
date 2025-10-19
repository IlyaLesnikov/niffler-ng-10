package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpendsRowMapper implements RowMapper<List<SpendEntity>> {
  private SpendsRowMapper() {}

  public static final SpendsRowMapper INSTANCE = new SpendsRowMapper();

  @Override
  public List<SpendEntity> mapRow(ResultSet rs, int rowNum) throws SQLException {
    List<SpendEntity> spendEntities = new ArrayList<>();
    while (rs.next()) {
      SpendEntity spendEntity = new SpendEntity();
      spendEntity.setId(rs.getObject("id", UUID.class));
      spendEntity.setUsername(rs.getString("username"));
      spendEntity.setCurrency(
          CurrencyValues.valueOf(rs.getString("currency"))
      );
      spendEntity.setSpendDate(rs.getDate("spend_date"));
      spendEntity.setAmount(rs.getDouble("amount"));
      spendEntity.setDescription(rs.getString("description"));
      spendEntities.add(spendEntity);
    }
    return spendEntities;
  }
}
