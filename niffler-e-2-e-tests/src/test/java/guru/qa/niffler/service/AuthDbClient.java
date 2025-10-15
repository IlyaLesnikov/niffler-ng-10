package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.impl.AuthUserDaoJdbc;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.TransactionIsolation;

import static guru.qa.niffler.data.Databases.transaction;

public class AuthDbClient {
  private static final Config CONFIG = Config.getInstance();

  public AuthUserJson createUser(AuthUserJson authUserJson) {
    AuthUserEntity authUserEntity = AuthUserEntity.fromJson(authUserJson);
    return transaction(connection -> {
      AuthUserEntity createdAuthUser = new AuthUserDaoJdbc(connection).create(authUserEntity);
      AuthorityEntity authorityRead = new AuthorityEntity();
      authorityRead.setId(null);
      authorityRead.setUser(createdAuthUser);
      authorityRead.setAuthority(Authority.read);
      AuthorityEntity authorityWrite = new AuthorityEntity();
      authorityRead.setId(null);
      authorityRead.setUser(createdAuthUser);
      authorityRead.setAuthority(Authority.read);
      AuthorityEntity createdAuthority = new AuthAuthorityDaoJdbc(connection).create(
          authorityRead,
          authorityWrite
      );
      createdAuthUser.setAuthorities(createdAuthority);
      return AuthUserJson.fromJson(createdAuthority);
    }, CONFIG.authJdbcUrl(), TransactionIsolation.TRANSACTION_NONE);
  }
}
