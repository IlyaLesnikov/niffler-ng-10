package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.AuthorityEntity;

import java.util.List;
import java.util.UUID;

public record AuthUserJson(
    UUID id,
    String username,
    String password,
    Boolean enabled,
    Boolean accountNonExpired,
    Boolean accountNonLocked,
    Boolean credentialsNonExpired,
    List<AuthorityEntity> authorities
) {

  public static AuthUserJson fromJson(AuthUserEntity authUserJson) {
    return new AuthUserJson(
        authUserJson.getId(),
        authUserJson.getUsername(),
        authUserJson.getPassword(),
        authUserJson.getEnabled(),
        authUserJson.getAccountNonExpired(),
        authUserJson.getAccountNonLocked(),
        authUserJson.getCredentialsNonExpired(),
        authUserJson.getAuthorities()
    );
  }
}
