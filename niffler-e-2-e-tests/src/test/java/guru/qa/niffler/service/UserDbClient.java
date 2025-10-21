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
import guru.qa.niffler.util.RandomDataUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDbClient {
  
  private final Config CONFIG = Config.getInstance();

  public UserJson createUser(UserJson userJson) {
    return Databases.xaTransaction(
        new XaFunction<>(
            connection -> {
              UserEntity user = UserEntity.fromJson(userJson);
              new UserDataDaoJdbc(connection).create(user);
              return null;
            },
            CONFIG.userDataJdbcUrl()
        ),
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
              AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values())
                  .map(authority -> {
                    AuthorityEntity authorityEntity = new AuthorityEntity();
                    authorityEntity.setUserId(createdAuthUserEntity.getId());
                    authorityEntity.setAuthority(authority);
                    return authorityEntity;
                  })
                  .toArray(AuthorityEntity[]::new);
              new AuthAuthorityDaoJdbc(connection).create(authorityEntities);
              createdAuthUserEntity.setAuthorities(Arrays.stream(authorityEntities).toList());
              return new UserJson(
                  createdAuthUserEntity.getId(),
                  userJson.username(),
                  userJson.firstname(),
                  userJson.surname(),
                  userJson.fullname(),
                  userJson.currency(),
                  userJson.photo(),
                  userJson.photoSmall()
              );
            },
            CONFIG.authJdbcUrl()
        )
    );
  }
}
