package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.model.User;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.util.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@WebTest
public class RegisterTest {

  private final Config CFG = Config.getInstance();

  @Test
  @DisplayName("Регистрация пользователя с валидными данными")
  void shouldRegisterUserWithValidData() {
    String password = RandomDataUtils.password();
    User user = new User(RandomDataUtils.username(), password, password);
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccountSubmit()
        .registerNewUser(user)
        .submitSignIn()
        .login(user.username(), user.password())
        .shouldStatisticsHeaderVisible()
        .shouldHistoryOfSpendingsVisible();
  }

  @Test
  @DisplayName("Регистрация пользоваталя с невалидным логином")
  void registeringUserWithInvalidUsernameTest() {
    User user = new User("aa", RandomDataUtils.password(), RandomDataUtils.password());
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccountSubmit()
        .registerNewUser(user)
        .shouldErrorMessageEqualText("Allowed username length should be from 3 to 50 characters");
  }

  @Test
  @DisplayName("Регистрация пользоваталя с невалидным паролем")
  void registeringUserWithInvalidPasswordTest() {
    User user = new User(RandomDataUtils.username(), "aa", "aa");
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccountSubmit()
        .registerNewUser(user)
        .shouldErrorMessageEqualText("Allowed password length should be from 3 to 12 characters");
  }

  @Test
  @DisplayName("Регистрация пользоваталя с разными паролями")
  void registeringUsersWithOtherUsersTest() {
    User user = new User(RandomDataUtils.username()  , RandomDataUtils.password(), RandomDataUtils.password());
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccountSubmit()
        .registerNewUser(user)
        .shouldErrorMessageEqualText("Passwords should be equal");
  }

  @Test
  @DisplayName("Авторизация под несуществующим пользователем")
  void authorizationUnderNonExistentUser() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("A", "B");
    new LoginPage().shouldErrorEqualText("Неверные учетные данные пользователя");
  }
}
