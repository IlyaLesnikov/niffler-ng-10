package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserDataDao;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.impl.UserDataDaoJdbc;
import guru.qa.niffler.model.UserJson;

public class UserDataDbClient {
  private final UserDataDao userDataDao = new UserDataDaoJdbc();

  public UserJson create(UserJson userJson) {
    UserEntity userEntity = UserEntity.fromJson(userJson);
    UserEntity createdUserEntity = userDataDao.create(userEntity);
    return UserJson.fromEntity(createdUserEntity);
  }

  public void delete(UserJson userJson) {
    UserEntity userEntity = UserEntity.fromJson(userJson);
    userDataDao.delete(userEntity);
  }
public class UserDbClient {
}
