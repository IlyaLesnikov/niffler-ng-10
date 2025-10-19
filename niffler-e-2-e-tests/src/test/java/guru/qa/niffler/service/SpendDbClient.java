package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

public class SpendDbClient implements SpendClient {
  private final Config CONFIG = Config.getInstance();
  private final SpendDao spendDao = new SpendDaoSpringJdbc(
      Databases.datasource(CONFIG.spendJdbcUrl())
  );
  private final CategoryDao categoryDao = new CategoryDaoSpringJdbc(
      Databases.datasource(CONFIG.spendJdbcUrl())
  );

  public SpendJson createSpend(SpendJson spendJson) {
    SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
    if (spendEntity.getCategory().getId() == null) {
      CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
      spendEntity.setCategory(categoryEntity);
    }

    return SpendJson.fromEntity(spendDao.create(spendEntity));
  }

  public CategoryJson createCategory(CategoryJson categoryJson) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
    CategoryEntity createdCategoryEntity = categoryDao.create(categoryEntity);
    return CategoryJson.fromEntity(createdCategoryEntity);
  }

  public CategoryJson updateCategory(CategoryJson categoryJson) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
    CategoryEntity updatedCategoryEntity = categoryDao.update(categoryEntity);
    return CategoryJson.fromEntity(updatedCategoryEntity);
  }
}