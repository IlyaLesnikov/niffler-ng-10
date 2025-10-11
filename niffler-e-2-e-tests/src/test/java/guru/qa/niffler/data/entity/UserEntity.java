package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@Setter
public class UserEntity implements Serializable {
  private UUID id;
  private String username;
  private CurrencyValues currency;
  private String firstname;
  private String surname;
  private String fullname;
  private byte[] photo;
  private byte[] photoSmall;

  public static @Nonnull UserEntity fromJson(@Nonnull UserJson userJson) {
    UserEntity userEntity = new UserEntity();
    userEntity.setId(userJson.id());
    userEntity.setUsername(userJson.username());
    userEntity.setCurrency(userJson.currency());
    userEntity.setFirstname(userJson.firstname());
    userEntity.setSurname(userJson.surname());
    userEntity.setFullname(userJson.fullname());
    userEntity.setPhoto(userJson.photo().getBytes(UTF_8));
    userEntity.setPhotoSmall(userJson.photoSmall().getBytes(UTF_8));
    return userEntity;
  }
}