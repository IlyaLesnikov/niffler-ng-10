package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.impl.SpendDaoJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

public class SpendDbClient {
  private final SpendDao spendDao = new SpendDaoJdbc();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();

  public SpendJson create(SpendJson spendJson) {
    SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
    if (spendEntity.getCategory().getId() == null) {
      CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
      spendEntity.setCategory(categoryEntity);
    }
    return SpendJson.fromEntity(spendDao.create(spendEntity));
  }

  public CategoryJson create(CategoryJson categoryJson) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
    CategoryEntity createdCategoryEntity = categoryDao.create(categoryEntity);
    return CategoryJson.fromEntity(createdCategoryEntity);
  }

  public CategoryJson update(CategoryJson categoryJson) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
    CategoryEntity updatedCategoryEntity = categoryDao.update(categoryEntity);
    return CategoryJson.fromEntity(updatedCategoryEntity);
  }
}
