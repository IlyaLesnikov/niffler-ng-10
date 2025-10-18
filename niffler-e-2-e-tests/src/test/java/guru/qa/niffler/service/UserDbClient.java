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
  private static final Config CONFIG = Config.getInstance();

  public Object createUser(UserJson userJson, AuthUserJson authUserJson, TransactionIsolation transactionIsolation) {
    return Databases.xaTransaction(
        transactionIsolation,
        new XaFunction<Object>(
            connection -> {
              UserEntity user = UserEntity.fromJson(userJson);
              return new UserDataDaoJdbc(connection).create(user);
            },
            CONFIG.userDataJdbcUrl()
        ),
        new XaFunction<>(
            connection -> {
              AuthUserEntity authUserEntity = AuthUserEntity.fromJson(authUserJson);
              AuthUserEntity createdAuthUserEntity = new AuthUserDaoJdbc(connection).create(authUserEntity);
              List<AuthorityEntity> createdAuthorityEntities = new AuthAuthorityDaoJdbc(connection).create(
                  AuthorityEntity.fromJson(
                      new AuthorityJson(null, AuthUserJson.fromEntity(createdAuthUserEntity), Authority.read)
                  ),
                  AuthorityEntity.fromJson(
                      new AuthorityJson(null, AuthUserJson.fromEntity(createdAuthUserEntity), Authority.write)
                  )
              );
              createdAuthUserEntity.setAuthorities(createdAuthorityEntities);
              return createdAuthUserEntity;
            },
            CONFIG.authJdbcUrl()
        )
    );
  }
}
