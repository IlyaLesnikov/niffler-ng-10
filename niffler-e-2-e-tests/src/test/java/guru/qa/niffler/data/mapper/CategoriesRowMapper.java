package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CategoriesRowMapper implements RowMapper<List<CategoryEntity>> {
  private CategoriesRowMapper() {}

  public static final CategoriesRowMapper INSTANCE = new CategoriesRowMapper();

  @Override
  public List<CategoryEntity> mapRow(ResultSet rs, int rowNum) throws SQLException {
    List<CategoryEntity> categoryEntities = new ArrayList<>();
    while (rs.next()) {
      CategoryEntity categoryEntity = new CategoryEntity();
      categoryEntity.setId(rs.getObject("id", UUID.class));
      categoryEntity.setUsername(rs.getString("username"));
      categoryEntity.setArchived(rs.getBoolean("archived"));
      categoryEntities.add(categoryEntity);
    }
    return categoryEntities;
  }
}
