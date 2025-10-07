package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
  CategoryEntity create(CategoryEntity categoryEntity);
  Optional<CategoryEntity> findById(UUID id);
  Optional<CategoryEntity> findByUsernameAndCategoryName(String username, String categoryName);
  List<CategoryEntity> findAllByUsername(String username);
  CategoryEntity update(CategoryEntity categoryEntity);
  void delete(CategoryEntity categoryEntity);
}
