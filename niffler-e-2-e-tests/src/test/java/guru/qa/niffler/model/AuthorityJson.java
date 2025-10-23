package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.AuthorityEntity;

import java.util.UUID;

public record AuthorityJson(
    UUID id
) {
  public static AuthorityJson fromEntity(AuthorityEntity authorityEntity) {
    return new AuthorityJson(authorityEntity.getId());
  }
}
