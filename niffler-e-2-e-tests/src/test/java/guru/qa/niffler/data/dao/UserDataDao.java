package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserDataDao {
  UserEntity create(UserEntity userEntity);
  Optional<UserEntity> findById(UUID id);
  Optional<UserEntity> findByUsername(String username);
  void delete(UserEntity user);
}
