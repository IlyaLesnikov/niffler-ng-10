package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CategoryRowMapper implements RowMapper<CategoryEntity> {
  private CategoryRowMapper() {}

  public static final CategoryRowMapper INSTANCE = new CategoryRowMapper();

  @Override
  public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setId(rs.getObject("id", UUID.class));
    categoryEntity.setUsername(rs.getString("username"));
    categoryEntity.setArchived(rs.getBoolean("archived"));
    return categoryEntity;
  }
}
