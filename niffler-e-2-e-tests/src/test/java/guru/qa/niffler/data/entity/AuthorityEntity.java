package guru.qa.niffler.data.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AuthorityEntity {
  private UUID id;
  private UUID userId;
  private Authority authority;
}
