package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.AuthorityJson;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AuthorityEntity {
  private UUID id;
  private AuthUserEntity user;
  private Authority authority;

  public static AuthorityEntity fromJson(AuthorityJson authorityJson) {
    AuthorityEntity authorityEntity = new AuthorityEntity();
    authorityEntity.setId(authorityEntity.getId());
    authorityEntity.setUser(
        AuthUserEntity.fromJson(authorityJson.user())
    );
    authorityEntity.setAuthority(authorityJson.authority());
    return authorityEntity;
  }
}
