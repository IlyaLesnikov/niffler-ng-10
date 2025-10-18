package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.impl.UserDataDaoJdbc;
import guru.qa.niffler.model.*;

import java.util.List;

public class UserDbClient {
  
  private final Config CONFIG = Config.getInstance();

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
