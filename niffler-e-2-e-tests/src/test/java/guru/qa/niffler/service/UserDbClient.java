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
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.XaFunction;
import guru.qa.niffler.util.RandomDataUtils;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class UserDbClient {
  
  private static final Config CONFIG = Config.getInstance();

  public UserJson createUser(UserJson userJson) {
    AtomicReference<UUID> id = new AtomicReference<>();
    return Databases.xaTransaction(
        new XaFunction<>(
            connection -> {
              AuthUserEntity authUserEntity = new AuthUserEntity();
              authUserEntity.setUsername(userJson.username());
              authUserEntity.setPassword(RandomDataUtils.password());
              authUserEntity.setEnabled(true);
              authUserEntity.setAccountNonExpired(true);
              authUserEntity.setAccountNonLocked(true);
              authUserEntity.setCredentialsNonExpired(true);
              AuthUserEntity createdAuthUserEntity = new AuthUserDaoJdbc(connection).create(authUserEntity);
              id.set(createdAuthUserEntity.getId());
              return null;
            },
            CONFIG.authJdbcUrl()
        ),
        new XaFunction<>(
            connection -> {
              AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values())
                  .map(authority -> {
                    AuthorityEntity authorityEntity = new AuthorityEntity();
                    authorityEntity.setUserId(id.get());
                    authorityEntity.setAuthority(authority);
                    return authorityEntity;
                  }).toArray(AuthorityEntity[]::new);
              new AuthAuthorityDaoJdbc(connection).create(authorityEntities);
              return null;
            },
            CONFIG.authJdbcUrl()
        ),
        new XaFunction<>(
            connection -> {
              UserEntity userEntity = UserEntity.fromJson(userJson);
              UserEntity createdUserEntity = new UserDataDaoJdbc(connection).create(userEntity);
              createdUserEntity.setId(id.get());
              return UserJson.fromEntity(createdUserEntity);
            },
            CONFIG.userDataJdbcUrl()
        )
    );
  }
}
