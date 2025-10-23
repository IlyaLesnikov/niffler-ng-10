package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.AuthUserEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
  AuthUserEntity create(AuthUserEntity authUserEntity);
  Optional<AuthUserEntity> findById(UUID id);
  void delete(AuthUserEntity authorityEntity);
}
