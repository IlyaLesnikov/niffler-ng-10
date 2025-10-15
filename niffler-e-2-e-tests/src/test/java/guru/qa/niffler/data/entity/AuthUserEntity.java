package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.AuthUserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AuthUserEntity implements Serializable {
  private UUID id;
  private String username;
  private String password;
  private Boolean enabled;
  private Boolean accountNonExpired;
  private Boolean accountNonLocked;
  private Boolean credentialsNonExpired;
  private List<AuthorityEntity> authorities = new ArrayList<>();

  public static AuthUserEntity fromJson(AuthUserJson authUserJson) {
    AuthUserEntity authorityEntity = new AuthUserEntity();
    authorityEntity.setId(authUserJson.id());
    authorityEntity.setUsername(authUserJson.username());
    authorityEntity.setPassword(authUserJson.password());
    authorityEntity.setEnabled(authUserJson.enabled());
    authorityEntity.setAccountNonExpired(authUserJson.accountNonExpired());
    authorityEntity.setAccountNonLocked(authUserJson.accountNonLocked());
    authorityEntity.setCredentialsNonExpired(authUserJson.accountNonExpired());
    authorityEntity.setAuthorities(authUserJson.authorities());
    return authorityEntity;
  }
}
