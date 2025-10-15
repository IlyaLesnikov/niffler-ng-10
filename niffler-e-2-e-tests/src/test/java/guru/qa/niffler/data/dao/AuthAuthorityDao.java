package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.AuthorityEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {
  AuthorityEntity create(AuthorityEntity... authorityEntities);
  Optional<AuthorityEntity> findById(UUID id);
  void delete(AuthorityEntity authorityEntity);
}
