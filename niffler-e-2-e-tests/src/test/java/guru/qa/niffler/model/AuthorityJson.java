package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.Authority;

import java.util.UUID;

public record AuthorityJson(
    UUID id,
    AuthUserJson user,
    Authority authority
) {
}
